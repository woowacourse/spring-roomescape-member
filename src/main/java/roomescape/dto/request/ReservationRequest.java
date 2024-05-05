package roomescape.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;

public record ReservationRequest(
        @NotBlank(message = "이름은 빈칸일 수 없어요") String name,
        @FutureOrPresent(message = "지난 날짜는 예약할 수 없어요") LocalDate date,
        @NotNull(message = "원하는 시간을 지정해 주세요") long timeId,
        @NotNull(message = "원하는 테마를 지정해 주세요") long themeId
) {
    public Reservation toEntity(final ReservationTime reservationTime, final Theme theme) {
        return Reservation.builder()
                .name(name)
                .date(date)
                .time(reservationTime)
                .theme(theme)
                .build();
    }
}
