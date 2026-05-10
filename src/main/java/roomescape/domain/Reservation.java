package roomescape.domain;

import java.time.LocalDate;
import lombok.Getter;
import roomescape.global.exception.InvalidReservationException;

@Getter
public class Reservation {

    private static final int MAX_NAME_LENGTH = 50;

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    private Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        validateName(name);
        validateNotNull(date, time, theme);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation createNew(String name, LocalDate date, ReservationTime time, Theme theme) {
        return new Reservation(null, name, date, time, theme);
    }

    public static Reservation from(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        return new Reservation(id, name, date, time, theme);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidReservationException("이름은 비어있을 수 없습니다.");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new InvalidReservationException("이름의 길이는 " + MAX_NAME_LENGTH + "자를 넘을 수 없습니다.");
        }
    }

    private void validateNotNull(LocalDate date, ReservationTime time, Theme theme) {
        if (date == null || time == null || theme == null) {
            throw new InvalidReservationException("예약 날짜, 시간, 테마는 필수입니다.");
        }
    }
}
