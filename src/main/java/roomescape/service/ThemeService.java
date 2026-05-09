package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.dao.row.AvailableTimeRow;
import roomescape.dao.row.ThemeRow;
import roomescape.domain.Theme;
import roomescape.domain.vo.Description;
import roomescape.domain.vo.Name;
import roomescape.domain.vo.ThumbnailUrl;
import roomescape.dto.request.PopularThemeRequestDto;
import roomescape.dto.request.ThemeRequestDto;
import roomescape.dto.response.ThemeResponseDto;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ThemeService {
    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public List<ThemeResponseDto> findAll() {
        return themeDao.findAll().stream()
                .map(ThemeResponseDto::from)
                .toList();
    }

    public ThemeResponseDto findById(Long id) {
        return themeDao.findById(id)
                .map(ThemeResponseDto::from)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 테마입니다."));
    }

    @Transactional
    public ThemeResponseDto create(ThemeRequestDto themeRequest) {
        Name name = new Name(themeRequest.name());

        if (themeDao.existsByName(name.value())) {
            throw new ConflictException("이미 존재하는 테마 이름입니다.");
        }

        Theme theme = Theme.create(
                name,
                new ThumbnailUrl(themeRequest.thumbnailUrl()),
                new Description(themeRequest.description()));

        ThemeRow saved = themeDao.create(ThemeRow.from(theme));
        return ThemeResponseDto.from(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (themeDao.findById(id).isEmpty()) {
            throw new NotFoundException("존재하지 않는 테마입니다.");
        }

        if (reservationDao.existsByThemeId(id)) {
            throw new ConflictException("예약이 존재하여 테마를 삭제할 수 없습니다.");
        }

        themeDao.delete(id);
    }

    public List<AvailableTimeRow> findAvailableTimesById(Long themeId, LocalDate localDate) {
        return themeDao.findAvailableTimesById(themeId, localDate);
    }

    public List<ThemeResponseDto> findPopulars(PopularThemeRequestDto popularThemeRequestDto) {
        return themeDao.findPopulars(popularThemeRequestDto).stream()
                .map(ThemeResponseDto::from)
                .toList();
    }
}
