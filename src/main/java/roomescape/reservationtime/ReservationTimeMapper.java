package roomescape.reservationtime;

import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.domain.dto.ReservationTimeReqDto;
import roomescape.reservationtime.domain.dto.ReservationTimeResDto;

public class ReservationTimeMapper {

    public static ReservationTime toEntity(ReservationTimeReqDto reqDto) {
        return new ReservationTime(reqDto.startAt());
    }

    public static ReservationTimeResDto toResDto(ReservationTime reservationTime) {
        return new ReservationTimeResDto(reservationTime.getId(), reservationTime.getStartAt());
    }
}
