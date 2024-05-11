package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ThemeRequestDto {
    @NotBlank(message = "테마 이름은 null이나 빈 값일 수 없습니다.")
    private String name;
    @NotBlank(message = "테마 설명은 null이나 빈 값일 수 없습니다.")
    private String description;
    @NotBlank(message = "테마 이미지는 null이나 빈 값일 수 없습니다.")
    private String thumbnail;

    public ThemeRequestDto() {
    }

    public ThemeRequestDto(
            final String name,
            final String description,
            final String thumbnail
    ) {
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
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
