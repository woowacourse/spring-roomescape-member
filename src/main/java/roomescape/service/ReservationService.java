package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.request.ReservationRequest;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

import java.time.LocalDateTime;

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

        return reservationRepository.save(reservation);
    }

    private void rejectPastTimeReservation(Reservation reservation) {
        LocalDateTime dateTime = LocalDateTime.of(reservation.getDate(), reservation.getTime().getStartAt());
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("이미 지난 시간입니다. 입력한 시간: " + dateTime.toLocalDate() + " "
                    + dateTime.toLocalTime());
        }
    }
}
