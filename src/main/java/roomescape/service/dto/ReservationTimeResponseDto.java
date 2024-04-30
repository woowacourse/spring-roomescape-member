package roomescape.service.dto;

import roomescape.domain.ReservationTime;

public class ReservationTimeResponseDto {

    private final long id;
    private final String startAt;

    public ReservationTimeResponseDto(ReservationTime reservationTime) {
        this.id = reservationTime.getId();
        this.startAt = reservationTime.getStartAt().toString();
    }

    public long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }
}
