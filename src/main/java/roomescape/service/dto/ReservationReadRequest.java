package roomescape.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ReservationReadRequest(@NotNull long themeId,
                                     @NotNull long memberId,
                                     @Pattern(regexp = "^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$",
                                             message = "올바르지 않은 날짜입니다.") String dateFrom,
                                     @Pattern(regexp = "^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$",
                                             message = "올바르지 않은 날짜입니다.") String dateTo) {
}
