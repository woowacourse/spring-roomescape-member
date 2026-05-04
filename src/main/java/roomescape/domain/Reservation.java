package roomescape.domain;

import java.time.LocalDate;

public class Reservation {
    private long id;
    private String name;
    private LocalDate date;
    private ReservationTime time;

    public Reservation(long id, String name, LocalDate date, ReservationTime time) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    private void validateName(final String name) {
        if (name.length() >= 10) {
            throw new IllegalArgumentException("[ERROR] 잘못된 이름 입력입니다.");
        }
    }

    public boolean isEqualTo(long id) {
        return this.id == id;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public ReservationTime getTime() {
        return this.time;
    }
}
