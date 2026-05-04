package roomescape.theme;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.ReservationRepository;

@Service
public class UserThemeService {

    private final ThemeRepository themeRepository;

    public UserThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
    }

    public List<ThemeResponse> findThemes(String sort, String order, LocalDate startDate, LocalDate endDate, Long limit) {
        List<Theme> themes = themeRepository.findAll(sort, order, startDate, endDate, limit);
        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
