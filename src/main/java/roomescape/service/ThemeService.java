package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.theme.dto.ThemeCreateRequest;
import roomescape.domain.theme.dto.ThemeResponse;
import roomescape.common.exception.BusinessException;
import roomescape.common.exception.ErrorCode;
import roomescape.repository.ReservationQueryingDao;
import roomescape.repository.ThemeQueryingDao;
import roomescape.repository.ThemeUpdatingDao;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class ThemeService {

    private final ThemeQueryingDao themeQueryingDao;
    private final ThemeUpdatingDao themeUpdatingDao;
    private final ReservationQueryingDao reservationQueryingDao;

    public ThemeService(ThemeQueryingDao themeQueryingDao, ThemeUpdatingDao themeUpdatingDao, ReservationQueryingDao reservationQueryingDao) {
        this.themeQueryingDao = themeQueryingDao;
        this.themeUpdatingDao = themeUpdatingDao;
        this.reservationQueryingDao = reservationQueryingDao;
    }

    @Transactional
    public ThemeResponse create(ThemeCreateRequest themeRequest) {
        Long id = themeUpdatingDao.insert(themeRequest);
        return ThemeResponse.from(themeQueryingDao.findThemeById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.THEME_NOT_FOUND)));
    }

    public List<ThemeResponse> findAll() {
        return themeQueryingDao.findAllTheme().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<ThemeResponse> findPopularTheme(Integer period, Integer limit) {
        return themeQueryingDao.findAllByTopTheme(period, limit).stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        if (reservationQueryingDao.existsReservationByThemeId(id)) {
            throw new BusinessException(ErrorCode.THEME_DELETE_CONFLICT);
        }
        themeUpdatingDao.delete(id);
    }
}
