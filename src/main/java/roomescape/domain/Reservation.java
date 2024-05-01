package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {

    private Long id;
    private Name name;
    private LocalDate date;
    private ReservationTime time;
    private Theme theme;

    public Reservation() {
    }

    public Reservation(Long id, String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        validateUnpassedDate(date, reservationTime.getStartAt());
        this.id = id;
        this.name = new Name(name);
        this.date = date;
        this.time = reservationTime;
        this.theme = theme;
    }

    public Reservation(String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        this(null, name, date, reservationTime, theme);
    }

    private void validateUnpassedDate(LocalDate date, LocalTime time) {
        if (date.isBefore(LocalDate.now()) && time.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("[ERROR] 지나간 날짜와 시간에 대한 예약 생성은 불가능합니다. : " + date + " " + time);
        }
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
}
