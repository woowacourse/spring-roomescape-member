package roomescape.domain;

import lombok.Getter;
import roomescape.global.exception.InvalidThemeException;

@Getter
public class Theme {

    private static final int MAX_VARCHAR_LENGTH = 255;
    private static final int MAX_URL_LENGTH = 512;

    private final Long id;
    private final String name;
    private final String description;
    private final String imageUrl;

    private Theme(Long id, String name, String description, String imageUrl) {
        validate(name, description, imageUrl);
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public static Theme createNew(String name, String description, String imageUrl) {
        return new Theme(null, name, description, imageUrl);
    }

    public static Theme from(Long id, String name, String description, String imageUrl) {
        return new Theme(id, name, description, imageUrl);
    }

    private void validate(String name, String description, String imageUrl) {
        if (name == null || name.isBlank()) {
            throw new InvalidThemeException("테마 이름은 비어있을 수 없습니다.");
        }
        if (name.length() > MAX_VARCHAR_LENGTH) {
            throw new InvalidThemeException("테마 이름은 " + MAX_VARCHAR_LENGTH + "자를 초과할 수 없습니다.");
        }

        if (description == null || description.isBlank()) {
            throw new InvalidThemeException("테마 설명은 비어있을 수 없습니다.");
        }
        if (description.length() > MAX_VARCHAR_LENGTH) {
            throw new InvalidThemeException("테마 설명은 " + MAX_VARCHAR_LENGTH + "자를 초과할 수 없습니다.");
        }

        if (imageUrl == null || imageUrl.isBlank()) {
            throw new InvalidThemeException("테마 이미지 URL은 비어있을 수 없습니다.");
        }
        if (imageUrl.length() > MAX_URL_LENGTH) {
            throw new InvalidThemeException("이미지 URL은 " + MAX_URL_LENGTH + "자를 초과할 수 없습니다.");
        }
    }
}
