package roomescape.domain;

import java.time.LocalDate;
import java.util.Objects;

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

    public static Reservation generateWithPrimaryKey(Reservation reservation, Long newPrimaryKey) {
        return new Reservation(newPrimaryKey, reservation.name, reservation.date, reservation.time, reservation.theme);
    }

    public boolean isSameId(Long id) {
        return Objects.equals(this.id, id);
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

    private void validate(String name, LocalDate date, ReservationTime time, Theme theme) {
        // TODO: 예외 분기 처리 좀 더 자세히
        if (name == null || date == null || time == null || theme == null) {
            throw new NullPointerException("예약 정보가 비어있습니다.");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("예약자명은 비워둘 수 없습니다.");
        }
    }
}
