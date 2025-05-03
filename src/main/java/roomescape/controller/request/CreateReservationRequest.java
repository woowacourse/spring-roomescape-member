package roomescape.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateReservationRequest(
    @JsonProperty("name")
    @NotEmpty
    String name,

    @JsonProperty("date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    LocalDate date,

    @JsonProperty("timeId")
    @NotNull
    Long timeId,

    @NotNull
    @JsonProperty("themeId")
    Long themeId
) {

}

