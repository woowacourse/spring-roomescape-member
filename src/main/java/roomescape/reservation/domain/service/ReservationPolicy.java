package roomescape.reservation.domain.service;

import java.time.LocalDateTime;
import roomescape.global.exception.RoomEscapeException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;

public class ReservationPolicy {

    public void validateReservable(
            Reservation reservation,
            ReservationTime reservationTime,
            LocalDateTime currentDateTime
    ) {
        if (isPast(reservation, reservationTime, currentDateTime)) {
            throw new RoomEscapeException("현재 시간보다 이전 시간으로 예약을 할 수 없습니다.");
        }
    }

    public void validateDeletable(
            Reservation reservation,
            ReservationTime reservationTime,
            LocalDateTime currentDateTime
    ) {
        if (isPast(reservation, reservationTime, currentDateTime)) {
            throw new RoomEscapeException("이미 지나간 예약은 삭제할 수 없습니다.");
        }
    }

    private boolean isPast(
            Reservation reservation,
            ReservationTime reservationTime,
            LocalDateTime currentDateTime
    ) {
        LocalDateTime reservationDateTime = LocalDateTime.of(reservation.getDate(), reservationTime.getStartAt());

        return reservationDateTime.isBefore(currentDateTime);
    }
}
