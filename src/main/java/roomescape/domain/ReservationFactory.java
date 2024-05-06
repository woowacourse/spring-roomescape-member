package roomescape.domain;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class ReservationFactory {
    public Reservation createReservation(String name, String date, ReservationTime time, Theme theme) {
        LocalDate reservationDate = LocalDate.parse(date);
        LocalTime reservationTime = time.getStartAt();
        LocalDateTime reservationDataTime = LocalDateTime.of(reservationDate, reservationTime);
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (reservationDataTime.isBefore(currentDateTime)) {
            throw new IllegalArgumentException(String.format("이미 지난 시간입니다. 입력한 예약 시간: %s %s",
                    reservationDataTime.toLocalDate(),
                    reservationDataTime.toLocalTime()));
        }

        return new Reservation(name, reservationDate, time, theme);
    }
}
