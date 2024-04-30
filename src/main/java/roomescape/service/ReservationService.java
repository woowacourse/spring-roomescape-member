package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.reservation.ReservationRequest;
import roomescape.controller.reservation.ReservationResponse;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    private Reservation assignTime(final Reservation reservation) {
        ReservationTime time = reservationTimeRepository
                .findById(reservation.id())
                .orElse(reservation.time());
        return reservation.assignTime(time);
    }

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll().stream()
                .map(this::assignTime)
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse addReservation(final ReservationRequest reservationRequest) {
        Reservation parsedReservation = reservationRequest.toDomain();
        Reservation savedReservation = reservationRepository.save(parsedReservation);

        ReservationTime time = reservationTimeRepository
                .findById(savedReservation.id())
                .orElse(savedReservation.time());
        Reservation assignedReservation = savedReservation.assignTime(time);

        return ReservationResponse.from(assignedReservation);
    }

    public int deleteReservation(final Long id) {
        return reservationRepository.deleteById(id);
    }
}
