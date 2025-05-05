package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.controller.theme.dto.AddThemeRequest;
import roomescape.controller.theme.dto.ThemeResponse;
import roomescape.model.Reservation;
import roomescape.model.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    @Autowired
    public ThemeService(final ReservationRepository reservationRepository, final ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    public ThemeResponse add(final AddThemeRequest request) {
        var theme = request.toEntity();
        var id = themeRepository.save(theme);
        var savedTheme = new Theme(id, theme.name(), theme.description(), theme.thumbnail());
        return ThemeResponse.from(savedTheme);
    }

    public List<ThemeResponse> findAll() {
        var themes = themeRepository.findAll();
        return ThemeResponse.from(themes);
    }

    public boolean removeById(final Long id) {
        List<Reservation> reservations = reservationRepository.findAllByThemeId(id);
        if (!reservations.isEmpty()) {
            throw new IllegalStateException("삭제하려는 테마를 사용하는 예약이 있습니다.");
        }
        return themeRepository.removeById(id);
    }

    public List<ThemeResponse> findPopularThemes(final LocalDate startDate, final LocalDate endDate, final Integer limit) {
        var themes = reservationRepository.findPopularThemesByPeriod(startDate, endDate, limit);
        return ThemeResponse.from(themes);
    }
}
