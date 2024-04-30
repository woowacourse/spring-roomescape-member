package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {

    private Long id;
    private Name name;
    private LocalDate date;
    private ReservationTime time;

    public Reservation() {
    }

    public Reservation(Long id, String name, LocalDate date, ReservationTime reservationTime) {
        validateUnpassedDate(date, reservationTime.getStartAt());
        this.id = id;
        this.name = new Name(name);
        this.date = date;
        this.time = reservationTime;
    }

    private void validateUnpassedDate(LocalDate date, LocalTime time) {
        if (date.isBefore(LocalDate.now()) && time.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("[ERROR] 지나간 날짜와 시간에 대한 예약 생성은 불가능합니다. : " + date + " " + time);
        }
    }

    public Reservation(String name, LocalDate date, ReservationTime reservationTime) {
        this(null, name, date, reservationTime);
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
}
