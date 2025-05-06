package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ThemeRequestDto(@JsonProperty("name") String name,
                              @JsonProperty("description") String description,
                              @JsonProperty("thumbnail") String thumbnail
) {
}

