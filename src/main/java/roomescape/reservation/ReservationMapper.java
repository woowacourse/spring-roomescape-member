package roomescape.reservation;

import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.dto.ReservationReqDto;
import roomescape.reservation.domain.dto.ReservationResDto;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.domain.dto.ReservationTimeResDto;
import roomescape.theme.domain.Theme;

public class ReservationMapper {

    public static Reservation toEntity(ReservationReqDto reqDto, ReservationTime reservationTime, Theme theme) {
        return Reservation.of(reqDto.name(), reqDto.date(), reservationTime, theme);
    }

    public static ReservationResDto toResDto(Reservation reservation, ReservationTimeResDto reservationTimeResDto) {
        return new ReservationResDto(reservation.getId(), reservation.getName(), reservation.getDate(),
            reservationTimeResDto);
    }
}
