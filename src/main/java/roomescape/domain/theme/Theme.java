package roomescape.domain.theme;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class Theme {
    private final Long id;
    private final ThemeName name;

    @NotBlank(message = "테마 설명은 필수입니다.")
    private final String description;

    @NotBlank(message = "썸네일 URL은 필수입니다.")
    @Pattern(regexp = "^https?://.*\\.(png|jpe?g|gif)$", message = "URL 형식에 맞지 않습니다.")
    private final String thumbnail;

    public Theme(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public Theme(Long id, String name, String description, String thumbnail) {
        this.id = id;
        this.name = new ThemeName(name);
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
