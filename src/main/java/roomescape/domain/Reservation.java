package roomescape.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final LocalDate createdAt;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, LocalDate createdAt, ReservationTime time, Theme theme) {
        validate(name, date, time, theme);
        if (createdAt != null && date.isBefore(createdAt)) {
            throw new IllegalArgumentException("과거 날짜로는 예약할 수 없습니다.");
        }
        this.id = id;
        this.name = name;
        this.date = date;
        this.createdAt = createdAt;
        this.time = time;
        this.theme = theme;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation that)) return false;
        if (id != null && that.id != null) {
            return id.equals(that.id);
        }
        return Objects.equals(name, that.name)
                && Objects.equals(date, that.date)
                && Objects.equals(time, that.time)
                && Objects.equals(theme, that.theme);
    }

    @Override
    public int hashCode() {
        if (id != null) return Objects.hash(id);
        return Objects.hash(name, date, time, theme);
    }

    private void validate(String name, LocalDate date, ReservationTime time, Theme theme) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("예약자 이름은 비어있을 수 없습니다.");
        }
        if (date == null) {
            throw new IllegalArgumentException("예약 날짜는 필수입니다.");
        }
        if (time == null) {
            throw new IllegalArgumentException("예약 시간은 필수입니다.");
        }
        if (theme == null) {
            throw new IllegalArgumentException("테마는 필수입니다.");
        }
    }
}
