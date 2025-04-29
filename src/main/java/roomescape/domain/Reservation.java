package roomescape.domain;

import java.time.LocalDate;

public class Reservation {

    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private Long id;

    public Reservation(final Long id, final String name, final LocalDate date, final ReservationTime time) {
        validateNameLength(name);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Reservation(final String name, final LocalDate date, final ReservationTime time) {
        validateNameLength(name);
        this.name = name;
        this.date = date;
        this.time = time;
    }

    private void validateNameLength(final String name) {
        if (name.length() > 255) {
            throw new IllegalArgumentException("이름은 255자 이하로 입력해야 합니다.");
        }
    }

    public boolean isEqualId(final Long id) {
        return this.id.equals(id);
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
