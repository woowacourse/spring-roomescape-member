package roomescape.domain;

import lombok.Getter;

@Getter
public class Theme {

    private static final int DESCRIPTION_MINIMUM_LENGTH = 5;

    private final Long id;
    private final ThemeName name;
    private final String description;
    private final String thumbnailUrl;

    private Theme(final Long id, final ThemeName name, final String description, final String thumbnailUrl) {
        this.id = id;
        this.name = name;
        validateDescription(description);
        this.description = description;
        validateThumbnailUrl(thumbnailUrl);
        this.thumbnailUrl = thumbnailUrl;
    }

    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("테마 설명란은 비워둘 수 없습니다.");
        }

        if (description.length() < DESCRIPTION_MINIMUM_LENGTH) {
            throw new IllegalArgumentException("테마 설명은 최소 5자 이상이어야 합니다.");
        }
    }

    private void validateThumbnailUrl(String thumbnailUrl) {
        if (thumbnailUrl == null || thumbnailUrl.isBlank()) {
            throw new IllegalArgumentException("썸네일 이미지 주소란은 비워둘 수 없습니다.");
        }
    }

    public static Theme create(final String name, final String description, final String thumbnailUrl) {
        return new Theme(
                null,
                new ThemeName(name),
                description,
                thumbnailUrl
        );
    }

    public static Theme createWithId(
            final Long id,
            final String name,
            final String description,
            final String thumbnailUrl
    ) {
        return new Theme(
                id,
                new ThemeName(name),
                description,
                thumbnailUrl
        );
    }

    public Theme withId(final Long id) {
        return new Theme(
                id,
                name,
                description,
                thumbnailUrl
        );
    }

    public String getName() {
        return name.getName();
    }
}
