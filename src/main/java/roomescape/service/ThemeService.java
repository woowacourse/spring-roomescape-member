package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.dao.ThemeDao;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.ThemeResponse;

@Service
public class ThemeService {
    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public ThemeResponse create(ThemeRequest request) {
        Theme theme = new Theme(
                null,
                request.name(),
                request.description(),
                request.url()
        );

        Theme saved = themeDao.save(theme);

        return ThemeResponse.from(saved);
    }

    public void delete(Long id) {
        themeDao.delete(id);
    }

    public List<ReservationTimeResponse> findAvailableTime(Long id, LocalDate date) {
        List<ReservationTime> availableTimes = themeDao.findAvailableTime(id, date);

        return availableTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }
}
