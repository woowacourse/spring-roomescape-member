package roomescape.domain;

import lombok.Getter;
import roomescape.global.exception.theme.InvalidThemeException;

@Getter
public class Theme {

    private static final int MAX_VARCHAR_LENGTH = 255;
    private static final int MAX_PATH_LENGTH = 512;
    private static final String IMAGE_PATH_PREFIX = "/images/themes/";

    private final Long id;
    private final String name;
    private final String description;
    private final String imagePath;

    private Theme(Long id, String name, String description, String imagePath) {
        validate(name, description, imagePath);
        this.id = id;
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
    }

    public static Theme createNew(String name, String description, String imagePath) {
        return new Theme(null, name, description, imagePath);
    }

    public static Theme from(Long id, String name, String description, String imagePath) {
        return new Theme(id, name, description, imagePath);
    }

    private void validate(String name, String description, String imagePath) {
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

        if (imagePath == null || imagePath.isBlank()) {
            throw new InvalidThemeException("테마 이미지 경로는 비어있을 수 없습니다.");
        }
        if (imagePath.length() > MAX_PATH_LENGTH) {
            throw new InvalidThemeException("이미지 경로는 " + MAX_PATH_LENGTH + "자를 초과할 수 없습니다.");
        }
        if (!imagePath.startsWith(IMAGE_PATH_PREFIX)) {
            throw new InvalidThemeException("테마 이미지 경로는 " + IMAGE_PATH_PREFIX + "로 시작해야 합니다.");
        }
    }
}
