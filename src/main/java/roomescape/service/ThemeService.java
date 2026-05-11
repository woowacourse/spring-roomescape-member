package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.exception.CustomException;
import roomescape.exception.ErrorCode;
import roomescape.service.dto.ServiceThemeRequest;
import roomescape.service.dto.ServiceThemeResponse;

@Service
@Transactional(readOnly = true)
public class ThemeService {
    public static final int RANKING_LIMIT = 10;

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }


    @Transactional
    public ServiceThemeResponse create(ServiceThemeRequest requestDto) {
        Theme theme = requestDto.toEntity();
        return ServiceThemeResponse.from(themeDao.create(theme));
    }

    public List<ServiceThemeResponse> readAll() {
        return themeDao.readAll().stream()
                .map(ServiceThemeResponse::from)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        if (reservationDao.existByThemeId(id)) {
            throw new CustomException(ErrorCode.REFERENCED_THEME);
        }
        themeDao.delete(id);
    }

    public List<ServiceThemeResponse> readRanking(LocalDate startDate, LocalDate endDate) {
        return themeDao.readRanking(startDate, endDate, RANKING_LIMIT).stream()
                .map(ServiceThemeResponse::from)
                .toList();
    }
}
