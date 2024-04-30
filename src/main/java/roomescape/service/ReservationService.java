package roomescape.service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationSaveRequest;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(final ReservationRepository reservationRepository, final ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse saveReservation(final ReservationSaveRequest reservationSaveRequest) {
        final ReservationTime time = reservationTimeRepository.findById(reservationSaveRequest.timeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다."));

        final Reservation reservation = reservationSaveRequest.toReservation(time);
        validateReservation(reservation);

        final Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(savedReservation);
    }

    private void validateReservation(final Reservation reservation) {
        validateDateTime(reservation);
        validateDuplicateTime(reservation);
    }

    private void validateDateTime(final Reservation reservation) {
        final LocalDateTime reservationDateTime = reservation.getDateTime();
        final boolean isBeforeNow = reservationDateTime.isBefore(LocalDateTime.now());
        if (isBeforeNow) {
            throw new IllegalArgumentException("지나간 시간입니다.");
        }
    }

    private void validateDuplicateTime(final Reservation reservation) {
        final boolean isDuplicated = reservationRepository.existByDateAndTimeId(reservation.getDate(), reservation.getTimeId());

        if (isDuplicated) {
            throw new IllegalArgumentException("이미 예약된 시간입니다.");
        }
    }

    public void deleteReservation(final Long id) {
        reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        reservationRepository.deleteById(id);
    }
}
