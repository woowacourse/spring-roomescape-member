package roomescape.service.dto;

import jakarta.validation.constraints.Pattern;

public record ReservationTimeCreateRequest(
        @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "올바르지 않은 시간입니다.") String startAt) {
}
