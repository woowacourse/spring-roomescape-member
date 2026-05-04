package roomescape.reservation.controller.dto;

import roomescape.reservation.domain.ReservationTime;

public class ReservationTimeResponseDto {
    private final Long id;
    private final String startAt;

    public ReservationTimeResponseDto(Long id, String startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTimeResponseDto from(ReservationTime time) {
        if (time == null) {
            return null;
        }
        return new ReservationTimeResponseDto(time.getId(), time.getStartAt());
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }
}
