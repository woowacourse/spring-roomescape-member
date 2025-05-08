package roomescape.service;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.dto.request.CreateReservationThemeRequest;
import roomescape.dto.response.ReservationThemeResponse;
import lombok.RequiredArgsConstructor;
import roomescape.domain.ReservationTheme;
import roomescape.repository.ReservationThemeRepository;

@Service
@RequiredArgsConstructor
public class ReservationThemeService {

    private final ReservationThemeRepository reservationThemeRepository;

    public ReservationThemeResponse create(CreateReservationThemeRequest request) {
        ReservationTheme reservationTheme = reservationThemeRepository.save(request.toReservationTheme());
        return ReservationThemeResponse.from(reservationTheme);
    }

    public List<ReservationThemeResponse> getAll() {
        List<ReservationTheme> reservationThemes = reservationThemeRepository.getAll();
        return reservationThemes.stream()
                .map(ReservationThemeResponse::from)
                .toList();
    }

    public void delete(Long id) {
        reservationThemeRepository.findById(id)
                .ifPresent(reservationThemeRepository::remove);
    }

    public List<ReservationThemeResponse> getPopularThemes(int limit) {
        List<ReservationTheme> reservationThemes = reservationThemeRepository.orderByThemeBookedCountWithLimit(limit);
        return reservationThemes.stream()
                .map(ReservationThemeResponse::from)
                .toList();
    }
}
