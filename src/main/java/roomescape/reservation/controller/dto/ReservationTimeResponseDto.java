package roomescape.reservation.controller.dto;

import roomescape.reservation.domain.ReservationTime;

public class ReservationTimeResponseDto {
    private final Long id;
    private final String startAt;
    private final String endAt;

    public ReservationTimeResponseDto(Long id, String startAt, String endAt) {
        this.id = id;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public static ReservationTimeResponseDto from(ReservationTime time) {
        if (time == null) {
            return null;
        }
        return new ReservationTimeResponseDto(time.getId(), time.getStartAt(), time.getEndAt());
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }

    public String getEndAt() {
        return endAt;
    }
}
