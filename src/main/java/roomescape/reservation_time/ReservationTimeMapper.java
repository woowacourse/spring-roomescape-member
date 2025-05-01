package roomescape.reservation_time;

import roomescape.reservation_time.domain.ReservationTime;
import roomescape.reservation_time.domain.dto.ReservationTimeReqDto;
import roomescape.reservation_time.domain.dto.ReservationTimeResDto;

public class ReservationTimeMapper {

    public static ReservationTime toEntity(ReservationTimeReqDto reqDto) {
        return new ReservationTime(reqDto.startAt());
    }

    public static ReservationTimeResDto toResDto(ReservationTime reservationTime) {
        return new ReservationTimeResDto(reservationTime.getId(), reservationTime.getStartAt());
    }
}
