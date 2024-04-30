package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.reservation.ReservationRequest;
import roomescape.controller.reservation.ReservationResponse;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.exception.TimeNotFoundException;
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

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll().stream()
                .map(this::assignTime)
                .map(ReservationResponse::from)
                .toList();
    }

    private Reservation assignTime(final Reservation reservation) {
        ReservationTime time = reservationTimeRepository
                .findById(reservation.getId())
                .orElse(reservation.getTime());
        return reservation.assignTime(time);
    }

    public ReservationResponse addReservation(final ReservationRequest reservationRequest) {
        ReservationTime time = reservationTimeRepository
                .findById(reservationRequest.timeId())
                .orElseThrow(() -> new TimeNotFoundException("존재하지 않은 시간입니다."));

        Reservation parsedReservation = reservationRequest.toDomain(time);
        Reservation savedReservation = reservationRepository.save(parsedReservation);

        System.out.println("reservationRequest = " + reservationRequest.timeId());

        return ReservationResponse.from(savedReservation);
    }

    public int deleteReservation(final Long id) {
        return reservationRepository.deleteById(id);
    }
}
