package roomescape.dto.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservation.ReservationName;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;

public record ReservationCreateRequest(
        @NotBlank(message = "예약자 이름을 입력해주세요.")
        String name,

        @NotBlank
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "yyyy-MM-dd 형식이 아닙니다.")
        String date,

        @NotNull(message = "예약 시간 ID를 입력해주세요.")
        Long timeId,

        @NotNull(message = "테마 ID를 입력해주세요.")
        Long themeId,

        @NotNull(message = "오늘 날짜를 입력해주세요.")
        LocalDate today,

        @NotNull(message = "현재 시간을 입력해주세요.")
        LocalTime now) {

    public static ReservationCreateRequest of(String name, String date, Long timeId, Long themeId, LocalDate today , LocalTime now) {
        return new ReservationCreateRequest(name, date, timeId, themeId, today, now);
    }

    public Reservation toDomain(ReservationTime reservationTime, Theme theme) {
        return new Reservation(
                null,
                new ReservationName(name),
                ReservationDate.from(date),
                reservationTime,
                theme
        );
    }
}
