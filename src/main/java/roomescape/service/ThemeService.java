package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.common.exception.DuplicatedException;
import roomescape.common.exception.ResourceInUseException;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.controller.theme.dto.ThemeRequestDto;
import roomescape.controller.theme.dto.ThemeResponseDto;
import roomescape.model.Theme;

@Service
public class ThemeService {

    private static final int POPULAR_DAY_RANGE = 7;
    private static final int THEME_QUERY_LIMIT = 10;

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public List<ThemeResponseDto> findAllThemes() {
        return themeDao.findAll().stream()
                .map(ThemeResponseDto::from)
                .collect(Collectors.toList());
    }

    public ThemeResponseDto saveTheme(ThemeRequestDto themeRequestDto) {
        validateTheme(themeRequestDto);

        Theme theme = themeRequestDto.convertToTheme();
        Long savedId = themeDao.saveTheme(theme);

        return new ThemeResponseDto(
                savedId,
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail()
        );
    }

    public void deleteTheme(Long id) {
        boolean isReserved = reservationDao.existsByThemeId(id);
        if (isReserved) {
            throw new ResourceInUseException("삭제하고자 하는 테마에 예약된 정보가 있습니다.");
        }
        themeDao.deleteById(id);
    }

    public List<ThemeResponseDto> findPopularThemes(final LocalDate today) {
        return themeDao.findThemesByReservationVolumeBetweenDates(today, POPULAR_DAY_RANGE, THEME_QUERY_LIMIT).stream()
                .map(ThemeResponseDto::from)
                .toList();
    }

    private void validateTheme(ThemeRequestDto themeRequestDto) {
        boolean duplicatedNameExisted = themeDao.isDuplicatedNameExisted(themeRequestDto.name());
        if (duplicatedNameExisted) {
            throw new DuplicatedException("중복된 테마는 등록할 수 없습니다.");
        }
    }
}