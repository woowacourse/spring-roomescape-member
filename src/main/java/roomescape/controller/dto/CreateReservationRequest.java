package roomescape.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public record CreateReservationRequest(
    @JsonProperty("name") String name,
    @JsonProperty("date") LocalDate date,
    @JsonProperty("timeId") Long timeSlotId,
    @JsonProperty("themeId") Long themeId
) {

    public CreateReservationRequest {
        if (name == null || date == null || timeSlotId == null || themeId == null) {
            throw new IllegalArgumentException("모든 값이 존재해야 합니다.");
        }
    }
}
