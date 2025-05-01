package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.common.exception.DuplicatedException;
import roomescape.common.exception.ResourceInUseException;
import roomescape.dao.ThemeDao;
import roomescape.dto.request.ThemeRequestDto;
import roomescape.dto.response.ThemeResponseDto;
import roomescape.model.Theme;

@Service
public class ThemeService {

    private static final int POPULAR_DAY_RANGE = 7;

    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<ThemeResponseDto> getAllThemes() {
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

    private void validateTheme(ThemeRequestDto themeRequestDto) {
        boolean duplicatedNameExisted = themeDao.isDuplicatedNameExisted(themeRequestDto.name());
        if (duplicatedNameExisted) {
            throw new DuplicatedException("중복된 예약시각은 등록할 수 없습니다.");
        }
    }

    public void deleteTheme(Long id) {
        try {
            themeDao.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceInUseException("삭제하고자 하는 테마에 예약된 정보가 있습니다.");
        }
    }

    public List<ThemeResponseDto> findPopularThemes(final String date) {
        LocalDate parsedDate = LocalDate.parse(date);

        return themeDao.findPopularThemes(parsedDate, POPULAR_DAY_RANGE).stream()
                .map(ThemeResponseDto::from)
                .toList();
    }
}
