package roomescape.reservation.service;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.error.ReservationException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public List<ReservationResponse> getReservations() {
        final List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public ReservationResponse saveReservation(final @Valid ReservationRequest request) {
        final ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다."));
        if (reservationRepository.existsByDateAndTime(request.date(), reservationTime.getStartAt())) {
            throw new ReservationException("해당 시간은 이미 예약되어있습니다.");
        }

        final Reservation reservation = new Reservation(request.name(), request.date(), reservationTime);
        final Reservation newReservation = reservationRepository.save(reservation);
        return new ReservationResponse(newReservation);
    }

    public void deleteReservation(final Long id) {
        final Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));
        reservationRepository.deleteById(reservation.getId());
    }
}
