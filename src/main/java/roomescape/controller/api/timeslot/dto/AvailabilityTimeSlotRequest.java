package roomescape.controller.api.timeslot.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AvailabilityTimeSlotRequest(

        @NotNull(message = "날짜는 필수입니다.")
        LocalDate date,

        @NotNull(message = "테마 ID는 필수입니다.")
        Long themeId
) {
}
