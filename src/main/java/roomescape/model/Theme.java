package roomescape.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@EqualsAndHashCode(of = {"id"})
@Getter
@Accessors(fluent = true)
@ToString
public class Theme {

    private static final int NAME_MAX_LENGTH = 10;
    private static final int DESCRIPTION_MAX_LENGTH = 50;

    private final Long id;
    private final String name;
    private final String description;
    // TODO: 별도의 thumbnail 자료형 구현하기
    private final String thumbnail;

    public Theme(final String name, final String description, final String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public Theme(final Long id, final String name, final String description, final String thumbnail) {
        validateNotNull(name, description, thumbnail);
        validateNameLength(name);
        validateDescriptionLength(description);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    private void validateNotNull(
        final String name,
        final String description,
        final String thumbnail
    ) {
        if (name == null || description == null || thumbnail == null) {
            throw new IllegalArgumentException("모든 값들이 존재해야 합니다.");
        }
    }

    private void validateNameLength(final String name) {
        if (name.length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("이름은 %d자를 넘길 수 없습니다.", NAME_MAX_LENGTH));
        }
    }

    private void validateDescriptionLength(final String description) {
        if (description.length() > DESCRIPTION_MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("설명은 %d자를 넘길 수 없습니다.", DESCRIPTION_MAX_LENGTH));
        }
    }
}
