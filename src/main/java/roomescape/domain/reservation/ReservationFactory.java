package roomescape.domain.reservation;

import org.springframework.stereotype.Component;
import roomescape.domain.theme.Theme;

import java.time.LocalDate;

@Component
public class ReservationFactory {
    public Reservation createReservation(String name, String date, ReservationTime time, Theme theme) {
        Reservation reservation = new Reservation(name, LocalDate.parse(date), time, theme);

        if (reservation.isBeforeNow()) {
            throw new IllegalArgumentException(String.format("이미 지난 시간입니다. 입력한 예약 시간: %s %s",
                    reservation.getDate().getStartAt(), reservation.getTime().getStartAt()));
        }

        return reservation;
    }
}
