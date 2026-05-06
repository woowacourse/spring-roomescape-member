package roomescape.reservation.controller.dto;

import roomescape.reservation.domain.ReservationTime;

public record ReservationTimeResponseDto(Long id, String startAt, String endAt) {

    public static ReservationTimeResponseDto from(ReservationTime time) {
        if (time == null) {
            return null;
        }
        return new ReservationTimeResponseDto(time.getId(), time.getStartAt(), time.getEndAt());
    }
}
