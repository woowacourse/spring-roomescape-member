package roomescape.reservationTime;

import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.domain.dto.ReservationTimeReqDto;
import roomescape.reservationTime.domain.dto.ReservationTimeResDto;

public class ReservationTimeMapper {

    public static ReservationTime toEntity(ReservationTimeReqDto reqDto) {
        return new ReservationTime(reqDto.startAt());
    }

    public static ReservationTimeResDto toResDto(ReservationTime reservationTime) {
        return new ReservationTimeResDto(reservationTime.getId(), reservationTime.getStartAt());
    }
}
