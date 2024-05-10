package roomescape.dto.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;

public record MemberReservationCreateRequest(
        @NotBlank
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "yyyy-MM-dd 형식이 아닙니다.")
        String date,

        @NotNull(message = "예약 시간 ID를 입력해주세요.")
        Long timeId,

        @NotNull(message = "테마 ID를 입력해주세요.")
        Long themeId) {

    public static MemberReservationCreateRequest of(String date, Long timeId, Long themeId) {
        return new MemberReservationCreateRequest(date, timeId, themeId);
    }

    public Reservation toDomain(Member member, ReservationTime reservationTime, Theme theme) {
        return new Reservation(
                null,
                ReservationDate.from(date),
                member,
                reservationTime,
                theme
        );
    }
}
