package roomescape.reservation.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.application.dto.response.ReservationThemeServiceResponse;
import roomescape.reservation.domain.entity.ReservationTheme;
import roomescape.reservation.domain.repository.ReservationThemeRepository;

@Service
@RequiredArgsConstructor
public class UserReservationThemeService {

    private final ReservationThemeRepository reservationThemeRepository;

    public List<ReservationThemeServiceResponse> getAll() {
        List<ReservationTheme> reservationThemes = reservationThemeRepository.getAll();
        return reservationThemes.stream()
                .map(ReservationThemeServiceResponse::from)
                .toList();
    }

    public List<ReservationThemeServiceResponse> getPopularThemes(int limit) {
        List<ReservationTheme> reservationThemes = reservationThemeRepository.getPopularThemesWithLimit(limit);
        return reservationThemes.stream()
                .map(ReservationThemeServiceResponse::from)
                .toList();
    }
}
