package roomescape.service.dto;

import roomescape.domain.ReservationTime;

public class AvailabilityOfTimeResponse {

    private final long id;
    private final String startAt;
    private final boolean alreadyBooked;

    public AvailabilityOfTimeResponse(ReservationTime reservationTime, boolean alreadyBooked) {
        this.id = reservationTime.getId();
        this.startAt = reservationTime.getStartAt().toString();
        this.alreadyBooked = alreadyBooked;
    }

    public long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }

    public Boolean getAlreadyBooked() {
        return alreadyBooked;
    }
}
