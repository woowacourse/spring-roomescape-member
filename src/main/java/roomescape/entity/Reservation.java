package roomescape.entity;

import java.time.LocalDate;
import java.time.Period;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;

    private Reservation(Long id, String name, LocalDate date, ReservationTime time) {
        if (name.length() > 10) {
            throw new IllegalArgumentException("이름은 10자를 넘어갈 수 없습니다.");
        }
        for (char c : name.toCharArray()) {
            if (Character.isDigit(c)) {
                throw new IllegalArgumentException("이름은 숫자를 포함할 수 없습니다.");
            }
        }
        if (time == null) {
            throw new IllegalArgumentException("존재하지 않는 시간입니다.");
        }
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public static Reservation beforeSave(final String name, final LocalDate date, final ReservationTime time) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("지나간 날짜는 예약할 수 없습니다.");
        }
        long minusDays = Period.between(date, LocalDate.now()).getDays();
        if (minusDays > 7) {
            throw new IllegalArgumentException("예약은 1주일 전부터 가능합니다.");
        }
        return new Reservation(null, name, date, time);
    }

    public static Reservation afterSave(Long id, String name, LocalDate date, ReservationTime time) {
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
