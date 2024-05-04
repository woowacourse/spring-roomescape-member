package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {
    private Long id;
    private String name;
    private LocalDate date;
    private ReservationTime time;
    private Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        validate(date, time);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        this(null, name, date, time, theme);
    }

    private void validate(LocalDate date, ReservationTime time) {
        if (!LocalDateTime.of(date, time.getStartAt()).isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("현재보다 이전 시간의 예약을 생성할 수 없습니다.");
        }
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
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}
