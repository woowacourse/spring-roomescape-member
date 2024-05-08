package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class Reservation {
    private final Long id;
    private final PlayerName name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;
    private final LocalDateTime createdAt;

    public Reservation(Long id, PlayerName name, LocalDate date, ReservationTime time, Theme theme,
                       LocalDateTime createdAt) {
        validateCreatedAtAfterReserveTime(date, time.getStartAt(), createdAt);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.createdAt = createdAt;
    }

    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme, LocalDateTime createdAt) {
        this(null, new PlayerName(name), date, time, theme, createdAt);
    }

    private void validateCreatedAtAfterReserveTime(LocalDate date, LocalTime startAt, LocalDateTime createdAt) {
        LocalDateTime reservedDateTime = LocalDateTime.of(date, startAt);
        if (reservedDateTime.isBefore(createdAt)) {
            throw new IllegalArgumentException("현재 시간보다 과거로 예약할 수 없습니다.");
        }
    }

    public boolean isBefore(LocalDateTime dateTime) {
        LocalDateTime reservedDateTime = LocalDateTime.of(date, time.getStartAt());
        return reservedDateTime.isBefore(dateTime);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Reservation withId(long id) {
        return new Reservation(id, name, date, time, theme, createdAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation that)) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
