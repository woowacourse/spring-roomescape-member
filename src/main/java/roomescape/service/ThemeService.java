package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.model.Reservation;
import roomescape.model.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public ThemeService(
        final ReservationRepository reservationRepository,
        final ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    public Theme register(final String name, final String description, final String thumbnail) {
        var theme = new Theme(name, description, thumbnail);
        var id = themeRepository.save(theme);
        return new Theme(id, name, description, thumbnail);
    }

    public List<Theme> findAllThemes() {
        return themeRepository.findAll();
    }

    public boolean removeById(long id) {
        List<Reservation> reservations = reservationRepository.findByThemeId(id);
        if (!reservations.isEmpty()) {
            throw new IllegalStateException("삭제하려는 테마를 사용하는 예약이 있습니다.");
        }
        return themeRepository.removeById(id);
    }

    public List<Theme> findPopularThemes(final LocalDate startDate, final LocalDate endDate, int count) {
        count = Math.min(count, 10);
        return themeRepository.findRankingByPeriod(startDate, endDate, count);
    }
}
