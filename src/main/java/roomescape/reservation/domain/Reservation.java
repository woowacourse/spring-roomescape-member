package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {
    private final Long id;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, LocalDate date, ReservationTime time, Theme theme) {
        validate(date, time);
        this.id = id;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(LocalDate date, ReservationTime time, Theme theme) {
        this(null, date, time, theme);
    }

    private void validate(LocalDate date, ReservationTime time) {
        if (!LocalDateTime.of(date, time.getStartAt()).isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("현재보다 이전 시간의 예약을 생성할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
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
}
