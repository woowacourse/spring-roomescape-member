package roomescape.service.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.web.exception.DateValid;

public record AdminReservationRequest(
        @NotBlank @Positive Long memberId,
        @NotBlank(message = "예약 날짜는 필수입니다.") @DateValid String date,
        @NotNull @Positive Long timeId,
        @NotNull @Positive Long themeId
) {

    public Reservation toDomain(Member member, ReservationTime reservationTime, Theme theme) {
        return new Reservation(member, new ReservationDate(LocalDate.parse(date)), reservationTime, theme);
    }
}
