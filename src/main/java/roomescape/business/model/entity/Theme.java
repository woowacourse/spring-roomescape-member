package roomescape.business.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import roomescape.business.model.vo.Id;
import roomescape.exception.business.InvalidCreateArgumentException;

@ToString
@EqualsAndHashCode(exclude = "id")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Theme {

    private static final int MAX_NAME_LENGTH = 20;

    private final Id id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public static Theme create(final String name, final String description, final String thumbnail) {
        validateMaxNameLength(name);
        return new Theme(Id.issue(), name, description, thumbnail);
    }

    public static Theme restore(final String id, final String name, final String description, final String thumbnail) {
        validateMaxNameLength(name);
        return new Theme(Id.create(id), name, description, thumbnail);
    }

    private static void validateMaxNameLength(final String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new InvalidCreateArgumentException("테마 이름은 %d자를 넘길 수 없습니다.".formatted(MAX_NAME_LENGTH));
        }
    }

    public String id() {
        return id.value();
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public String thumbnail() {
        return thumbnail;
    }
}
