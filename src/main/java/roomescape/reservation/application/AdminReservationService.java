package roomescape.reservation.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.application.dto.request.ReservationSearchServiceRequest;
import roomescape.reservation.application.dto.response.ReservationServiceResponse;
import roomescape.reservation.domain.entity.Reservation;
import roomescape.reservation.domain.repository.ReservationRepository;

@Service
@RequiredArgsConstructor
public class AdminReservationService {

    private final ReservationRepository reservationRepository;

    public List<ReservationServiceResponse> getAll() {
        List<Reservation> reservations = reservationRepository.getAll();
        return reservations.stream()
                .map(ReservationServiceResponse::from)
                .toList();
    }

    public List<ReservationServiceResponse> getSearchedAll(ReservationSearchServiceRequest request) {
        List<Reservation> reservations = reservationRepository.getSearchReservations(
                request.themeId(),
                request.memberId(),
                request.dateFrom(),
                request.dateTo()
        );
        return reservations.stream()
                .map(ReservationServiceResponse::from)
                .toList();
    }

    public void delete(Long id) {
        Reservation reservation = reservationRepository.getById(id);
        reservationRepository.remove(reservation);
    }
}
