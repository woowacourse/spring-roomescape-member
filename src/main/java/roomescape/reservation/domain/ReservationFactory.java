package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import roomescape.exception.business.PastTimeReservationException;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

@Component
public class ReservationFactory {

    public Reservation create(String name, LocalDate date, ReservationTime time, Theme theme) {
        validate(name, date, time, theme);
        return Reservation.restore(null, name, date, time, theme);
    }

    private void validate(String name, LocalDate date, ReservationTime time, Theme theme) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("예약자 이름은 필수입니다.");
        }
        if (date == null) {
            throw new IllegalArgumentException("날짜는 필수입니다.");
        }
        if (time == null || time.getId() == null) {
            throw new IllegalArgumentException("예약 시간은 필수입니다.");
        }
        if (LocalDateTime.of(date, time.getStartAt()).isBefore(LocalDateTime.now())) {
            throw new PastTimeReservationException("이미 지난 시간에는 예약할 수 없습니다.");
        }
        if (theme == null) {
            throw new IllegalArgumentException("테마는 필수입니다.");
        }
    }
}