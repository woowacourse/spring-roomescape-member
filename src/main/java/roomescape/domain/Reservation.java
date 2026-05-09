package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "id")
public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final Theme theme;
    private final ReservationTime time;

    public Reservation(Long id, String name, LocalDate date, Theme theme, ReservationTime time) {
        validate(name, date, theme, time);

        this.id = id;
        this.name = name;
        this.date = date;
        this.theme = theme;
        this.time = time;
    }

    public static Reservation of(String name, LocalDate date, Theme theme, ReservationTime time) {
        Reservation reservation = new Reservation(null, name, date, theme, time);
        LocalDateTime reservationDateTime = time.toReservationDateTime(date);

        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("현재보다 이전 날짜로 예약할 수 없습니다.");
        }

        return reservation;
    }

    private static void validate(String name, LocalDate date, Theme theme, ReservationTime time) {
        validateName(name);
        validateTheme(theme);
        validateDateTime(date, time);
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("예약자 정보는 비어있을 수 없습니다.");
        }
    }

    private static void validateTheme(Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("테마 정보는 비어있을 수 없습니다.");
        }
    }

    private static void validateDateTime(LocalDate date, ReservationTime time) {
        if (date == null || time == null) {
            throw new IllegalArgumentException("예약 날짜 및 시간 정보는 비어있을 수 없습니다.");
        }
    }
}
