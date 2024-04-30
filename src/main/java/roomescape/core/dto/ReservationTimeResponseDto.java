package roomescape.core.dto;

import roomescape.core.domain.ReservationTime;

public class ReservationTimeResponseDto {
    private Long id;
    private String startAt;

    public ReservationTimeResponseDto() {
    }

    public ReservationTimeResponseDto(final Long id, final String startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTimeResponseDto(final ReservationTime reservationTime) {
        this(reservationTime.getId(), reservationTime.getStartAt());
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }
}
