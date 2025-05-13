package roomescape.controller.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.model.Member;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

public record AdminReservationRequestDto(
        @NotNull(message = "예약 날짜는 null이거나 공백일 수 없습니다")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @NotNull(message = "예약 시각은 null 일 수 없습니다")
        Long timeId,

        @NotNull(message = "테마는 null 일 수 없습니다")
        Long themeId,

        @NotNull(message = "유저는 null 일 수 없습니다")
        Long memberId
) {
    public Reservation convertToReservation(
            ReservationTime reservationTime,
            Theme theme,
            Member member
    ) {
        return new Reservation(member, date, reservationTime, theme);
    }

}
