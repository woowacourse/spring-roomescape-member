package roomescape.service.dto;

import jakarta.validation.constraints.NotBlank;
import roomescape.domain.Theme;

public class ThemeRequestDto {

    @NotBlank(message = "이름은 반드시 입력되어야 합니다.")
    private final String name;

    @NotBlank(message = "설명은 반드시 입력되어야 합니다.")
    private final String description;

    @NotBlank(message = "썸네일은 반드시 입력되어야 합니다.")
    private final String thumbnail;

    public ThemeRequestDto(String name, String description, String thumbnail) {
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme toTheme() {
        return new Theme(null, name, description, thumbnail);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
