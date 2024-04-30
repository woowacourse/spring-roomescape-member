package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.reservation.ReservationRequest;
import roomescape.controller.reservation.ReservationResponse;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.exception.PreviousTimeException;
import roomescape.exception.TimeNotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

import java.time.LocalDateTime;
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
        final ReservationTime time = reservationTimeRepository
                .findById(reservationRequest.timeId())
                .orElseThrow(() -> new TimeNotFoundException("존재하지 않은 시간입니다."));
        final Reservation parsedReservation = reservationRequest.toDomain(time);

        final LocalDateTime reservationDateTime = parsedReservation.getDate().atTime(time.getStartAt());
        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new PreviousTimeException("지난 시간으로 예약할 수 없습니다.");
        }

        Reservation savedReservation = reservationRepository.save(parsedReservation);
        return ReservationResponse.from(savedReservation);
    }

    public int deleteReservation(final Long id) {
        return reservationRepository.deleteById(id);
    }
}
