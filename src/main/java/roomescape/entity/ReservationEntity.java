package roomescape.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReservationEntity {
    private final Long id;
    private String name;
    private LocalDate date;
    private ReservationTimeEntity time;

    public ReservationEntity(Long id, String name, LocalDate date, ReservationTimeEntity time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public boolean isDuplicatedWith(ReservationEntity other) {
        return date.isEqual(other.date) && time.isDuplicatedWith(other.time);
    }

    public String getFormattedDate() {
        return date.toString();
    }

    public LocalDateTime getDateTime() {
        return LocalDateTime.of(date, time.getStartAt());
    }

    public Long getTimeId() {
        return time.getId();
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

    public ReservationTimeEntity getTime() {
        return time;
    }
}
