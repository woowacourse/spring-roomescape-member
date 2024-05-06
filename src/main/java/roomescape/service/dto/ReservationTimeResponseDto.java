package roomescape.service.dto;

import roomescape.domain.ReservationTime;

public class ReservationTimeResponseDto {

    private final long id;
    private final String startAt;

    public ReservationTimeResponseDto(long id, String startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTimeResponseDto(ReservationTime reservationTime) {
        this(reservationTime.getId(), reservationTime.getStartAt().toString());
    }

    public long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }
}
