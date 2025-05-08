package roomescape.controller.reservation.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import roomescape.model.Member;
import roomescape.model.Reservation;
import roomescape.model.Theme;
import roomescape.model.TimeSlot;

public record AddReservationRequest(

        @NotNull(message = "멤버 ID는 필수입니다.")
        Long memberId,

        @NotNull(message = "날짜는 필수입니다.")
        @FutureOrPresent(message = "날짜는 현재보다 미래여야 합니다.")
        LocalDate date,

        @NotNull(message = "시간 ID는 필수입니다.")
        Long timeId,

        @NotNull(message = "테마 ID는 필수입니다.")
        Long themeId
) {

    public Reservation toEntity(final Member member, final TimeSlot timeSlot, final Theme theme) {
        return new Reservation(member, date, timeSlot, theme);
    }
}
