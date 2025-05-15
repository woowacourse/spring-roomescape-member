package roomescape.dto.reservation.response;

import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.dto.member.response.MemberResponse;
import roomescape.dto.reservationtime.response.ReservationTimeResponseDto;
import roomescape.dto.theme.response.ThemeResponseDto;

public record ReservationResponseDto(
        Long id,
        MemberResponse member,
        String date,
        ReservationTimeResponseDto time,
        ThemeResponseDto theme) {

    public static ReservationResponseDto from(final Reservation reservation) {
        Member member = reservation.getMember();
        return new ReservationResponseDto(
                reservation.getId(),
                MemberResponse.from(member),
                reservation.getDate().toString(),
                ReservationTimeResponseDto.from(reservation.getReservationTime()),
                ThemeResponseDto.from(reservation.getTheme())
        );
    }

}
