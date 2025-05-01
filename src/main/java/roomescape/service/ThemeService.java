package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.model.Reservation;
import roomescape.model.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    @Autowired
    public ThemeService(ReservationRepository reservationRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    public Theme add(String name, String description, String thumbnail) {
        var theme = Theme.create(name, description, thumbnail);
        var id = themeRepository.save(theme);
        return Theme.register(id, name, description, thumbnail);
    }

    public List<Theme> allThemes() {
        return themeRepository.findAll();
    }

    public boolean removeById(long id) {
        List<Reservation> reservations = reservationRepository.findByThemeId(id);
        if (!reservations.isEmpty()) {
            throw new IllegalStateException("삭제하려는 테마를 사용하는 예약이 있습니다.");
        }
        return themeRepository.removeById(id);
    }

    public List<Theme> findPopularThemes(LocalDate startDate, LocalDate endDate, Integer limit) {
        return themeRepository.findRankingByPeriod(startDate, endDate, limit);
    }
}
