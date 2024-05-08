package roomescape.theme.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.JdbcReservationRepository;
import roomescape.theme.dto.request.CreateThemeRequest;
import roomescape.theme.dto.response.CreateThemeResponse;
import roomescape.theme.dto.response.FindPopularThemesResponse;
import roomescape.theme.dto.response.FindThemeResponse;
import roomescape.theme.model.Theme;
import roomescape.theme.repository.JdbcThemeRepository;

@Service
public class ThemeService {

    private final JdbcThemeRepository themeRepository;
    private final JdbcReservationRepository reservationRepository;

    public ThemeService(
            final JdbcThemeRepository themeRepository,
            final JdbcReservationRepository reservationRepository
    ) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public CreateThemeResponse createTheme(CreateThemeRequest createThemeRequest) {
        Theme theme = themeRepository.save(createThemeRequest.toTheme());
        return CreateThemeResponse.of(theme);
    }

    public List<FindThemeResponse> getThemes() {
        List<Theme> themes = themeRepository.findAll();
        return themes.stream()
                .map(FindThemeResponse::of)
                .toList();
    }

    public List<FindPopularThemesResponse> getPopularThemes() {
        return themeRepository.findOrderByReservation().stream()
                .map(FindPopularThemesResponse::of)
                .toList();
    }

    public void deleteById(final Long id) {
        List<Reservation> reservations = reservationRepository.findAllByThemeId(id);
        if (!reservations.isEmpty()) {
            throw new IllegalStateException("테마를 사용 중인 예약이 존재합니다.");
        }
        themeRepository.deleteById(id);
    }
}
