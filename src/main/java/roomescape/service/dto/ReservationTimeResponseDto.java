package roomescape.service.dto;

import roomescape.domain.ReservationTime;

public class ReservationTimeResponseDto {

    private final long id;
    private final String startAt;
    private final Boolean alreadyBooked;

    public ReservationTimeResponseDto(ReservationTime reservationTime, Boolean alreadyBooked) {
        this.id = reservationTime.getId();
        this.startAt = reservationTime.getStartAt().toString();
        this.alreadyBooked = alreadyBooked;
    }

    public ReservationTimeResponseDto(ReservationTime reservationTime) {
        this(reservationTime, null);
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
