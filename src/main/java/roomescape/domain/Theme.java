package roomescape.domain;

import java.net.URI;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "id")
public class Theme {
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnailImageUrl;

    public Theme(Long id, String name, String description, String thumbnailImageUrl) {
        validateTheme(name, description, thumbnailImageUrl);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    public Theme(String name, String description, String thumbnailImageUrl) {
        this(null, name, description, thumbnailImageUrl);
    }

    private static void validateTheme(String name, String description, String thumbnailImageUrl) {
        validateName(name);
        validateDescription(description);
        validateThumbnailImageUrl(thumbnailImageUrl);
    }

    private static void validateThumbnailImageUrl(String thumbnailImageUrl) {
        if (isInvalidImageUrl(thumbnailImageUrl)) {
            throw new IllegalArgumentException("올바른 이미지 주소 형식이 아닙니다.");
        }
    }

    private static boolean isInvalidImageUrl(String thumbnailImageUrl) {
        try {
            URI uri = URI.create(thumbnailImageUrl);
            return uri.getScheme() == null || !uri.getScheme().startsWith("http");
        } catch (Exception e) {
            throw new IllegalArgumentException("올바른 이미지 주소 형식이 아닙니다.");
        }
    }

    private static void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("설명은 필수 값입니다.");
        }
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 필수 값입니다.");
        }
    }
}
