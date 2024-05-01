package roomescape.domain;

import java.time.LocalDate;

public class Reservation {

    private final Long id;
    private final UserName name;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(Long id, UserName name, LocalDate date, ReservationTime time) {
        validate(date);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Reservation(UserName name, LocalDate date, ReservationTime time) {
        this(null, name, date, time);
    }

    private void validate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("예약 날짜가 입력되지 않았습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public UserName getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getReservationTime() {
        return time;
    }
}
