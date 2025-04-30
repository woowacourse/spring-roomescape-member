package roomescape.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateThemeRequest(
    @JsonProperty("name") String name,
    @JsonProperty("description") String description,
    @JsonProperty("thumbnail") String thumbnail
) {

    public CreateThemeRequest {
        if (name == null || description == null || thumbnail == null) {
            throw new IllegalArgumentException("모든 값들이 존재해야 합니다.");
        }
    }
}
