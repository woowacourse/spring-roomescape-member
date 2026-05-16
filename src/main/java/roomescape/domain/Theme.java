package roomescape.domain;

import java.net.URI;
import lombok.Getter;
import roomescape.global.exception.InactiveException;
import roomescape.global.exception.ValidationException;

@Getter
public class Theme {
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnailImageUrl;
    private final boolean isActive;

    private Theme(Long id, String name, String description, String thumbnailImageUrl, boolean isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.isActive = isActive;
    }

    public static Theme create(String name, String description, String thumbnailImageUrl) {
        validate(name, description, thumbnailImageUrl);
        return new Theme(null, name, description, thumbnailImageUrl, true);
    }

    public static Theme restore(Long id, String name, String description, String thumbnailImageUrl, boolean isActive) {
        return new Theme(id, name, description, thumbnailImageUrl, isActive);
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
                throw new ValidationException("올바른 이미지 주소 형식이 아닙니다. url=" + thumbnailImageUrl);
            }
        } catch (Exception e) {
            throw new ValidationException("올바른 이미지 주소 형식이 아닙니다. url=" + thumbnailImageUrl);
        }
    }

    private static void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new ValidationException("설명은 필수 값입니다.");
        }
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("이름은 필수 값입니다.");
        }
    }

    public Theme deactivate() {
        return restore(id, name, description, thumbnailImageUrl, false);
    }

    public void validateInactive() {
        if (!isActive()) {
            throw new InactiveException("비활성화 된 테마는 예약할 수 없습니다.");
        }
    }
}
