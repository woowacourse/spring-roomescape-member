package roomescape.service.dto;

import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.repository.projection.ReservationEntity;

public class ReservationResult {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTimeResult time;
    private final ThemeResult theme;

    public ReservationResult(
            Long id,
            String name,
            LocalDate date,
            ReservationTimeResult time,
            ThemeResult theme
    ) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static ReservationResult from(ReservationEntity entity) {
        Reservation reservation = entity.getReservation();
        return new ReservationResult(
                entity.getId(),
                reservation.getName(),
                reservation.getDate(),
                new ReservationTimeResult(entity.getTimeId(), reservation.getTime().getStartAt()),
                new ThemeResult(
                        entity.getThemeId(),
                        reservation.getTheme().getName(),
                        reservation.getTheme().getDescription(),
                        reservation.getTheme().getThumbnailUrl()
                )
        );
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

    public ReservationTimeResult getTime() {
        return time;
    }

    public ThemeResult getTheme() {
        return theme;
    }
}
