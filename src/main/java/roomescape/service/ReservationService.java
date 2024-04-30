package roomescape.service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import roomescape.controller.request.ReservationRequest;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public Reservation save(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = reservationTimeRepository.findById(reservationRequest.timeId())
                .orElseThrow(() -> new IllegalArgumentException("예약할 수 없는 시간입니다. timeId: " + reservationRequest.timeId()));
        Reservation reservation = reservationRequest.toEntity(reservationTime);


        rejectPastTimeReservation(reservation);
        rejectDuplicateDateTime(reservation);

        return reservationRepository.save(reservation);
    }

    private void rejectDuplicateDateTime(Reservation reservation) {
        LocalDateTime reservationDateTime = LocalDateTime.of(reservation.getDate(), reservation.getTime().getStartAt());
        boolean present = reservationRepository.findAll().stream()
                .map(savedReservation -> LocalDateTime.of(savedReservation.getDate(), savedReservation.getTime().getStartAt()))
                .anyMatch(dateTime -> dateTime.equals(reservationDateTime));
        if (present) {
            throw new IllegalArgumentException("중복된 예약이 존재합니다.");
        }
    }

    private void rejectPastTimeReservation(Reservation reservation) {
        LocalDateTime dateTime = LocalDateTime.of(reservation.getDate(), reservation.getTime().getStartAt());
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("이미 지난 시간입니다. 입력한 시간: " + dateTime.toLocalDate() + " "
                    + dateTime.toLocalTime());
        }
    }
}
