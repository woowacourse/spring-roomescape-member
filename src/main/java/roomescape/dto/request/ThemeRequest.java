package roomescape.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ThemeRequest(@JsonProperty("name") String name,
                           @JsonProperty("description") String description,
                           @JsonProperty("thumbnail") String thumbnail
) {
}

