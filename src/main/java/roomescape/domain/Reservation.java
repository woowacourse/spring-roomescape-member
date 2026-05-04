package roomescape.domain;

import java.time.LocalDate;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;


    public Reservation(Long id, String name, LocalDate date, ReservationTime time) {
        this.id = id;
        validateNameFormat(name);
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Reservation(String name, LocalDate date, ReservationTime time) {
        this.id = 0L;
        validateNameFormat(name);
        this.name = name;
        this.date = date;
        this.time = time;
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

    private void validateNameFormat(String name) {
        if(name == null || name.isBlank()){
            throw new IllegalArgumentException("이름 형식은 2글자 이상 10글자 이하입니다");
        }

        if(name.length()<2|| name.length()>10){
            throw new IllegalArgumentException("이름 형식은 2글자 이상 10글자 이하입니다");
        }
    }
}
