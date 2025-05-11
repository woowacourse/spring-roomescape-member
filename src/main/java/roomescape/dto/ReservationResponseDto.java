package roomescape.dto;

import roomescape.domain.Member;
import roomescape.domain.Reservation;

public record ReservationResponseDto(
        Long id,
        MemberResponse member,
        String date,
        ReservationTimeResponseDto time,
        ThemeResponseDto theme) {

    public static ReservationResponseDto from(Reservation reservation) {
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
