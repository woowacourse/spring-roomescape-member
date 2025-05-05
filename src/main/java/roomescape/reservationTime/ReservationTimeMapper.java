package roomescape.reservationTime;

import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.domain.dto.ReservationTimeRequestDto;
import roomescape.reservationTime.domain.dto.ReservationTimeResponseDto;

public class ReservationTimeMapper {

    public static ReservationTime toEntity(ReservationTimeRequestDto requestDto) {
        return new ReservationTime(requestDto.startAt());
    }

    public static ReservationTimeResponseDto toResponseDto(ReservationTime reservationTime) {
        return new ReservationTimeResponseDto(reservationTime.getId(), reservationTime.getStartAt());
    }
}
