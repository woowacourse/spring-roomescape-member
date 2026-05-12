package roomescape.domain;

import java.net.URI;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class Theme {
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnailImageUrl;
    private boolean isActive;

    public Theme(Long id, String name, String description, String thumbnailImageUrl, boolean isActive) {
        validate(name, description, thumbnailImageUrl);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.isActive = isActive;
    }

    public Theme(String name, String description, String thumbnailImageUrl) {
        this(null, name, description, thumbnailImageUrl, true);
    }

    private static void validate(String name, String description, String thumbnailImageUrl) {
        validateName(name);
        validateDescription(description);
        validateThumbnailImageUrl(thumbnailImageUrl);
    }

    private static void validateThumbnailImageUrl(String thumbnailImageUrl) {
        try {
            URI uri = URI.create(thumbnailImageUrl);

            if (uri.getScheme() == null || !uri.getScheme().startsWith("http")) {
                throw new IllegalArgumentException("올바른 이미지 주소 형식이 아닙니다. url=" + thumbnailImageUrl);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("올바른 이미지 주소 형식이 아닙니다. url=" + thumbnailImageUrl);
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

    public void deactivate() {
        if (!isActive) {
            throw new IllegalArgumentException("이미 비활성화 된 테마입니다.");
        }
        this.isActive = false;
    }
}
