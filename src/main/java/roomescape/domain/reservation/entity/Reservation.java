package roomescape.domain.reservation.entity;

import java.time.LocalDate;
import roomescape.domain.reservation.dto.response.ReservationCreateResponseDTO;
import roomescape.domain.reservation.dto.response.ReservationResponseDTO;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.time.entity.Time;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final Time time;
    private final Theme theme;

    private Reservation(Long id, String name, LocalDate date, Time time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
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

    public Theme getTheme() {
        return theme;
    }

    public ReservationResponseDTO toResponseDTO() {
        return new ReservationResponseDTO(id, name, date, time.toResponseDTO(), theme.toResponseDTO());
    }

    public ReservationCreateResponseDTO toCreateResponseDTO() {
        return new ReservationCreateResponseDTO(id, name, date, time.getId(), theme.getId());
    }

    public static Reservation create(String name, LocalDate date, Time time, Theme theme) {
        return new Reservation(null, name, date, time, theme);
    }

    public static Reservation reconstruct(Long id, String name, LocalDate date, Time time, Theme theme) {
        return new Reservation(id, name, date, time, theme);
    }
}
