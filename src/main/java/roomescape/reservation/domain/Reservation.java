package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import roomescape.common.domain.Id;
import roomescape.reservationtime.domain.ReservationTime;

public class Reservation {
    private final Id id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;

    private Reservation(final Id id, final String name, final LocalDate date, final ReservationTime time) {
        validateNameLength(name);
        validateDateTime(date, time.getStartAt());
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public static Reservation of(final Long id, final String name, final LocalDate date, final ReservationTime time) {
        return new Reservation(Id.from(id), name, date, time);
    }

    public static Reservation withUnassignedId(final String name, final LocalDate date, final ReservationTime time) {
        return new Reservation(Id.unassigned(), name, date, time);
    }

    private void validateNameLength(final String value) {
        if (value.length() > 10) {
            throw new IllegalArgumentException("이름은 10글자 이내여야 합니다.");
        }
    }

    private void validateDateTime(LocalDate date, LocalTime time) {
        if (LocalDateTime.of(date, time).isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("예약 시간이 현재 시간보다 이전일 수 없습니다.");
        }
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

    public Long getId() {
        return id.getValue();
    }

    public void setId(Long value) {
        id.setValue(value);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName())
                && Objects.equals(getDate(), that.getDate()) && Objects.equals(getTime(),
                that.getTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDate(), getTime());
    }
}
