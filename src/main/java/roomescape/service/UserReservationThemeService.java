package roomescape.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import roomescape.domain.ReservationTheme;
import roomescape.repository.ReservationThemeRepository;
import roomescape.service.dto.response.ReservationThemeServiceResponse;

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
        List<ReservationTheme> reservationThemes = reservationThemeRepository.orderByThemeBookedCountWithLimit(limit);
        return reservationThemes.stream()
                .map(ReservationThemeServiceResponse::from)
                .toList();
    }
}
