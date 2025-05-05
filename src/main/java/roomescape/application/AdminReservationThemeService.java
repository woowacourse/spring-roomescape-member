package roomescape.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.entity.ReservationTheme;
import roomescape.domain.repository.ReservationThemeRepository;
import roomescape.application.dto.request.CreateReservationThemeServiceRequest;
import roomescape.application.dto.response.ReservationThemeServiceResponse;

@Service
@RequiredArgsConstructor
public class AdminReservationThemeService {

    private final ReservationThemeRepository reservationThemeRepository;

    public ReservationThemeServiceResponse create(CreateReservationThemeServiceRequest request) {
        ReservationTheme reservationTheme = reservationThemeRepository.save(request.toReservationTheme());
        return ReservationThemeServiceResponse.from(reservationTheme);
    }

    public void delete(Long id) {
        // TODO: noContent vs IllegalArgumentException
        ReservationTheme reservationTheme = reservationThemeRepository.getById(id);
        reservationThemeRepository.remove(reservationTheme);
    }
}
