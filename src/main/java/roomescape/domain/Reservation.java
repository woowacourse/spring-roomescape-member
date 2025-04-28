package roomescape.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final Long timeId;

    public Reservation(Long id, String name, LocalDate date, Long timeId) {
        validate(name, date, timeId);
        this.id = id;
        this.name = name;
        this.date = date;
        this.timeId = timeId;
    }

    public static Reservation generateWithPrimaryKey(Reservation reservation, Long newPrimaryKey) {
        return new Reservation(newPrimaryKey, reservation.name, reservation.date, reservation.timeId);
    }

    public boolean isSameId(Long id) {
        return Objects.equals(this.id, id);
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

    public Long getTimeId() {
        return timeId;
    }

    private void validate(String name, LocalDate date, Long timeId) {
        if (name == null || date == null || timeId == null) {
            throw new NullPointerException("예약 정보가 비어있습니다.");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("예약자명은 비워둘 수 없습니다.");
        }
    }
}
