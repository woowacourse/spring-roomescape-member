package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {
    private final Long id;
    private final String name;
    private final Theme theme;
    private LocalDate date;
    private Time time;

    public Reservation(Long id, String name, LocalDate date, Time time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(String name, LocalDate date, Time time, Theme theme) {
        this(null, name, date, time, theme);
    }

    public void validateCreate(LocalDateTime now) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time.getStartAt());
        if (reservationDateTime.isBefore(now)) {
            throw new IllegalArgumentException("지난 시간에 대한 예약 생성은 불가능합니다. 관리자에게 문의하세요");
        }
    }

    public void validateCancel(LocalDateTime now) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time.getStartAt());
        if (reservationDateTime.isBefore(now)) {
            throw new IllegalArgumentException("지난 예약은 취소 불가능합니다. 관리자에게 문의하세요");
        }
    }

    public void update(LocalDate date, Time time) {
        this.date = date;
        this.time = time;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(date);
        result = 31 * result + Objects.hashCode(time);
        result = 31 * result + Objects.hashCode(theme);
        return result;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Reservation that)) {
            return false;
        }

        return Objects.equals(id, that.id) && Objects.equals(name, that.name)
                && Objects.equals(date, that.date) && Objects.equals(time, that.time)
                && Objects.equals(theme, that.theme);
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

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
