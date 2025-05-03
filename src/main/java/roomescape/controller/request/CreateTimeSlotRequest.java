package roomescape.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record CreateTimeSlotRequest(
    @JsonProperty("startAt")
    @JsonFormat(pattern = "HH:mm")
    @NotNull
    LocalTime startAt
) {

}
