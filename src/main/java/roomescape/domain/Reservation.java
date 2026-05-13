package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        validate(name, date, time, theme);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        validate(name, date, time, theme);
        this.id = null;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation of(Long id, Reservation reservation) {
        return new Reservation(id, reservation.name, reservation.date, reservation.time, reservation.theme);
    }

    private void validate(String name, LocalDate date, ReservationTime time, Theme theme) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 이름은 비어 있을 수 없습니다.");
        }
        if (date == null) {
            throw new IllegalArgumentException("[ERROR] 날짜는 비어 있을 수 없습니다.");
        }
        if (time == null) {
            throw new IllegalArgumentException("[ERROR] 예약 시간은 비어 있을 수 없습니다.");
        }
        if (theme == null) {
            throw new IllegalArgumentException("[ERROR] 테마는 비어 있을 수 없습니다.");
        }
    }

    public boolean isPast(LocalDateTime now) {
        LocalDate nowDate = now.toLocalDate();
        LocalTime nowTime = now.toLocalTime();

        if (date.isBefore(nowDate)) {
            return true;
        }
        if (date.isAfter(nowDate)) {
            return false;
        }
        return time.isPast(nowTime);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
