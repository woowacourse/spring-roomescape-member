package roomescape.application;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import roomescape.application.dto.response.ReservationThemeServiceResponse;
import roomescape.domain.entity.ReservationTheme;
import roomescape.domain.repository.ReservationThemeRepository;

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
