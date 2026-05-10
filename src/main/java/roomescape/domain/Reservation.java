package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.theme = theme;
        validateNameFormat(name);
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        this(null, name, date, time, theme);
    }

    public static Reservation create(String name, LocalDate date, ReservationTime time, Theme theme, LocalDateTime now) {
        validateNotPast(date, time, now);
        return new Reservation(name, date, time, theme);
    }

    public Long getTimeId() {
        return time.getId();
    }

    public Long getThemeId() {
        return theme.getId();
    }

    private void validateNameFormat(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름 형식은 2글자 이상 10글자 이하입니다");
        }

        if (name.length() < 2 || name.length() > 10) {
            throw new IllegalArgumentException("이름 형식은 2글자 이상 10글자 이하입니다");
        }
    }

    private static void validateNotPast(LocalDate date, ReservationTime time, LocalDateTime now) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time.getStartAt());

        if (reservationDateTime.isBefore(now)) {
            throw new IllegalArgumentException("지난 날짜 또는 시간은 예약할 수 없습니다.");
        }
    }
}
