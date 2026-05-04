package roomescape.reservation.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

public record ReservationRequest(
        @NotBlank(message = "이름은 필수입니다.")
        String name,
        @NotNull(message = "날짜는 필수입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        @NotNull(message = "시간 ID는 필수입니다.")
        Long timeId,
        @NotNull(message = "테마 ID는 필수입니다.")
        Long themeId
) {
    public static Reservation toEntity(ReservationRequest request, ReservationTime time, Theme theme) {
        return Reservation.builder()
                .name(request.name)
                .date(request.date)
                .time(time)
                .theme(theme)
                .build();
    }
}
