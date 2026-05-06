package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.AvailableReservationTimeResponse;
import roomescape.dto.ThemeResponse;
import roomescape.repository.ThemeDao;

@Service
public class ThemeService {
    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<ThemeResponse> getAllThemes() {
        return themeDao.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<AvailableReservationTimeResponse> getAvailableTimeResponses(Long themId, String date) {
        return themeDao.findAvailableTimeById(themId, date);
    }
}
