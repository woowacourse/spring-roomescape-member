package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import roomescape.domain.time.ReservationTime;
import roomescape.domain.user.UserName;

public class Reservation {
    private final Long id;
    private final UserName name;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(String name, LocalDate date, ReservationTime time) {
        this(null, name, date, time);
    }

    public Reservation(Long id, String name, LocalDate date, ReservationTime time) {
        this(id, new UserName(name), date, time);
    }

    public Reservation(Long id, Reservation reservation) {
        this(id, reservation.name, reservation.date, reservation.time);
    }

    private Reservation(Long id, UserName name, LocalDate date, ReservationTime time) {
        Objects.requireNonNull(date, "예약 날짜는 필수입니다.");
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time.getStartAt();
    }

    public Long getTimeId() {
        return time.getId();
    }
}
