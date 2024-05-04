package roomescape.reservation.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import roomescape.reservation.model.Reservation;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.theme.model.Theme;

public record CreateReservationRequest(
        @FutureOrPresent(message = "예약 날짜는 현재보다 과거일 수 없습니다.")
        @NotNull(message = "예약 등록 시 예약 날짜는 필수입니다.")
        LocalDate date,

        @NotBlank(message = "예약자 명은 공백 문자가 불가능합니다.")
        @Size(message = "예약자 명은 최대 255자까지 입력이 가능합니다.", max = 255)
        String name,

        @Positive(message = "예약 시간 식별자는 양수만 가능합니다.")
        @NotNull(message = "예약 등록 시 시간은 필수입니다.")
        Long timeId,

        @Positive(message = "예약 테마 식별자는 양수만 가능합니다.")
        @NotNull(message = "예약 등록 시 테마는 필수입니다.")
        Long themeId) {
    public Reservation toReservation(final ReservationTime reservationTime, final Theme theme) {
        return new Reservation(
                null,
                this.name,
                this.date,
                reservationTime,
                theme);
    }
}
