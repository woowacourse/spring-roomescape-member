package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

import java.time.LocalDate;

public record ReservationSaveRequest(
        @NotBlank(message = "예약자 이름이 비어 있습니다.")
        String name,

        @NotNull(message = "예약 날짜가 비어 있습니다.")
        @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate date,

        @NotNull(message = "예약 시간 아이디가 비어 있습니다.")
        Long timeId,

        @NotNull(message = "예약 테마 아이디가 비어 있습니다.")
        Long themeId
) {

    public Reservation toReservation(final ReservationTime reservationTime, final Theme theme) {
        return new Reservation(name, date, reservationTime, theme);
    }
}
