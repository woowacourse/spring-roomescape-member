package roomescape.reservation.dto;

import roomescape.member.dto.MemberResponseDto;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.dto.ThemeResponseDto;
import roomescape.time.dto.ReservationTimeResponseDto;

import java.time.LocalDate;

public record ReservationResponseDto(long id, MemberResponseDto member, LocalDate date,
                                     ReservationTimeResponseDto time,
                                     ThemeResponseDto theme) {

    public ReservationResponseDto(final Reservation reservation) {
        this(reservation.getId(),
                new MemberResponseDto(reservation.getMember()),
                reservation.getDate(),
                new ReservationTimeResponseDto(reservation.getTime()),
                new ThemeResponseDto(reservation.getTheme()));
    }
}
