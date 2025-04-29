package roomescape.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.domain.repository.ReservationTimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationResponse> getAll() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse add(ReservationRequest request) {
        ReservationTime findTime = reservationTimeRepository.findById(request.timeId());
        Reservation reservation = request.toReservationWithoutId(findTime);

        Long id = reservationRepository.saveAndReturnId(reservation);
        return ReservationResponse.from(reservation.withId(id));
    }

    public void remove(Long id) {
        reservationRepository.deleteById(id);
    }

}
