package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReservationFactory {

    public Reservation create(String name, String date, ReservationTime time, Theme theme) {
        LocalDate reservationDate = LocalDate.parse(date);
        LocalTime reservationStartAt = time.getStartAt();
        LocalDateTime reservationDateTime = LocalDateTime.of(reservationDate, reservationStartAt);
        LocalDateTime nowDateTime = LocalDateTime.now();

        if (reservationDateTime.isBefore(nowDateTime)) {
            throw new IllegalStateException("이미 지나간 시간에 대한 예약을 할 수 없습니다.");
        }

        return new Reservation(new Name(name), LocalDate.parse(date), time, theme);
    }
}
