package roomescape.reservation;

import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.dto.ReservationReqDto;
import roomescape.reservation.domain.dto.ReservationResDto;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.domain.dto.ReservationTimeResDto;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.dto.ThemeResDto;

public class ReservationMapper {

    public static Reservation toEntity(
        Member member,
        ReservationReqDto reqDto,
        ReservationTime reservationTime,
        Theme theme
    ) {
        return Reservation.withoutId(member.getName(), reqDto.date(), reservationTime, theme);
    }

    public static ReservationResDto toResDto(
        Reservation reservation,
        Member member,
        ReservationTimeResDto reservationTimeResDto,
        ThemeResDto themeResDto
    ) {
        return new ReservationResDto(
            reservation.getId(),
            member,
            reservation.getDate(),
            reservationTimeResDto,
            themeResDto
        );
    }
}
