package roomescape.repository.projection;

import roomescape.domain.Reservation;


public class ReservationEntity {

    private final Long id;
    private final Long timeId;
    private final Long themeId;
    private final Reservation reservation;

    public ReservationEntity(Long id, Long timeId, Long themeId, Reservation reservation) {
        this.id = id;
        this.timeId = timeId;
        this.themeId = themeId;
        this.reservation = reservation;
    }

    public Long getId() {
        return id;
    }

    public Long getTimeId() {
        return timeId;
    }

    public Long getThemeId() {
        return themeId;
    }

    public Reservation getReservation() {
        return reservation;
    }
}
