package roomescape.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

public record CreateThemeRequest(
    @JsonProperty("name")
    @NotEmpty
    String name,

    @JsonProperty("description")
    @NotEmpty
    String description,

    @JsonProperty("thumbnail")
    @NotEmpty
    String thumbnail
) {

}
