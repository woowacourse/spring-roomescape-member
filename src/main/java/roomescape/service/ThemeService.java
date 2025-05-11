package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.controller.api.theme.dto.AddThemeRequest;
import roomescape.controller.api.theme.dto.ThemeResponse;
import roomescape.exception.RoomescapeException;
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
        final Theme theme = request.toEntity();
        final Long id = themeRepository.save(theme);
        final Theme savedTheme = new Theme(id, theme.name(), theme.description(), theme.thumbnail());
        return ThemeResponse.from(savedTheme);
    }

    public List<ThemeResponse> findAll() {
        final List<Theme> themes = themeRepository.findAll();
        return ThemeResponse.from(themes);
    }

    public boolean removeById(final Long id) {
        final List<Reservation> reservations = reservationRepository.findAllByThemeId(id);
        if (!reservations.isEmpty()) {
            throw new RoomescapeException("삭제하려는 테마를 사용하는 예약이 있습니다. 삭제하려는 테마 ID: " + id);
        }
        return themeRepository.removeById(id);
    }

    public List<ThemeResponse> findPopularThemes(final LocalDate startDate, final LocalDate endDate,
                                                 final Integer limit) {
        final List<Theme> themes = reservationRepository.findPopularThemesByPeriod(startDate, endDate, limit);
        return ThemeResponse.from(themes);
    }
}
