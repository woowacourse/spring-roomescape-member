package roomescape.reservation.domain;

import java.time.LocalDate;

public class Reservation {

    private Long id;
    private String name;
    private LocalDate date;
    private ReservationTime time;

    public Reservation() {
    }

    private Reservation(Long id, String name, LocalDate date, ReservationTime time) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    private void validateName(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 예약자 이름은 공백일 수 없습니다.");
        }
    }

    public Reservation(String name, LocalDate date, ReservationTime time) {
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public static Reservation of(Long id, String name, LocalDate date, ReservationTime time) {
        return new Reservation(id, name, date, time);
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
}
