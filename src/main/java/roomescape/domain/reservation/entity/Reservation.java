package roomescape.domain.reservation.entity;

import java.time.LocalDate;
import roomescape.domain.reservation.dto.response.ReservationResponseDTO;
import roomescape.domain.time.entity.Time;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final Time time;

    private Reservation(Long id, String name, LocalDate date, Time time) {
        this.id = id;
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

    public Time getTime() {
        return time;
    }

    public Long getTimeId() {
        return time.getId();
    }

    public ReservationResponseDTO toResponseDTO() {
        return new ReservationResponseDTO(id, name, date, time.toResponseDTO());
    }

    public static Reservation create(String name, LocalDate date, Time time) {
        return new Reservation(null, name, date, time);
    }

    public static Reservation reconstruct(Long id, String name, LocalDate date, Time time) {
        return new Reservation(id, name, date, time);
    }
}
