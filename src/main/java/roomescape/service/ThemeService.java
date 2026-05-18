package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.ThemeErrorCode;
import roomescape.common.exception.RestApiException;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationQueryDao;
import roomescape.dao.ThemeDao;
import roomescape.dao.row.AvailableTimeRow;
import roomescape.dao.row.ThemeRow;
import roomescape.domain.Theme;
import roomescape.domain.vo.Description;
import roomescape.domain.vo.Name;
import roomescape.domain.vo.ThumbnailUrl;
import roomescape.dto.request.ThemeRequestDto;
import roomescape.dto.response.ThemeResponseDto;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ThemeService {
    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;
    private final ReservationQueryDao reservationQueryDao;

    public ThemeService(ThemeDao themeDao, ReservationDao reservationDao, ReservationQueryDao reservationQueryDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
        this.reservationQueryDao = reservationQueryDao;
    }

    public List<ThemeResponseDto> findAll() {
        return themeDao.findAll().stream()
                .map(ThemeResponseDto::from)
                .toList();
    }

    public ThemeResponseDto findById(Long id) {
        return themeDao.findById(id)
                .map(ThemeResponseDto::from)
                .orElseThrow(() -> new RestApiException(ThemeErrorCode.NOT_FOUND));
    }

    @Transactional
    public ThemeResponseDto create(ThemeRequestDto themeRequest) {
        Name name = new Name(themeRequest.name());

        if (themeDao.existsByName(name.value())) {
            throw new RestApiException(ThemeErrorCode.DUPLICATE_NAME);
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
        if (!themeDao.existsById(id)) {
            throw new RestApiException(ThemeErrorCode.NOT_FOUND);
        }

        if (reservationDao.existsByThemeId(id)) {
            throw new RestApiException(ThemeErrorCode.REFERENCED_BY_RESERVATION);
        }

        themeDao.delete(id);
    }

    public List<AvailableTimeRow> findAvailableTimesById(Long themeId, LocalDate localDate) {
        return reservationQueryDao.findAvailableTimesById(themeId, localDate);
    }

    public List<ThemeResponseDto> findPopulars(int limit, int days, LocalDate date) {
        return reservationQueryDao.findPopulars(limit, days, date).stream()
                .map(ThemeResponseDto::from)
                .toList();
    }
}

