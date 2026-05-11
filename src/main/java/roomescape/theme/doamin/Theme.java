package roomescape.theme.doamin;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "id")
public class Theme {
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnailUrl;

    public Theme(Long id, String name, String description, String thumbnailUrl) {
        validate(name, description, thumbnailUrl);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
    }

    public static Theme of(String name, String description, String thumbnailUrl) {
        return new Theme(null, name, description, thumbnailUrl);
    }

    private void validate(String name, String description, String thumbnailUrl) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("테마 이름은 필수입니다.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("테마 설명은 필수입니다.");
        }
        if (thumbnailUrl == null || thumbnailUrl.isBlank()) {
            throw new IllegalArgumentException("썸네일 URL은 필수입니다.");
        }
    }
}
