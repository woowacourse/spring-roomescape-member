package roomescape.domain.theme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ThemeCreateRequest {

    @Size(max = 255, message = "이름은 255자 이하여야 합니다.")
    @NotBlank(message = "이름은 필수입니다.")
    private final String name;

    private final String description;

    private final String url;

    public ThemeCreateRequest(String name, String description, String url) {
        this.name = name;
        this.description = description;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }
}
