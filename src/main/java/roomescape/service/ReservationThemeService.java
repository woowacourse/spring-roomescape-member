package roomescape.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import roomescape.domain.ReservationTheme;
import roomescape.repository.ReservationThemeRepository;
import roomescape.service.dto.request.CreateReservationThemeServiceRequest;
import roomescape.service.dto.response.ReservationThemeServiceResponse;

@Service
@RequiredArgsConstructor
public class ReservationThemeService {

    private final ReservationThemeRepository reservationThemeRepository;

    public ReservationThemeServiceResponse create(CreateReservationThemeServiceRequest request) {
        ReservationTheme reservationTheme = reservationThemeRepository.save(request.toReservationTheme());
        return ReservationThemeServiceResponse.from(reservationTheme);
    }

    public List<ReservationThemeServiceResponse> getAll() {
        List<ReservationTheme> reservationThemes = reservationThemeRepository.getAll();
        return reservationThemes.stream()
                .map(ReservationThemeServiceResponse::from)
                .toList();
    }

    public void delete(Long id) {
        // TODO: noContent vs IllegalArgumentException
        ReservationTheme reservation = reservationThemeRepository.getById(id);
        reservationThemeRepository.remove(reservation);
    }

    public List<ReservationThemeServiceResponse> getPopularThemes(int limit) {
        List<ReservationTheme> reservationThemes = reservationThemeRepository.orderByThemeBookedCountWithLimit(limit);
        return reservationThemes.stream()
                .map(ReservationThemeServiceResponse::from)
                .toList();
    }
}
