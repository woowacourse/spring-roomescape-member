package roomescape.domain.auth.entity;

import lombok.Getter;
import roomescape.common.exception.InvalidArgumentException;

@Getter
public class Name {

    private static final int MAX_NAME_LENGTH = 25;

    private final String name;

    public Name(final String name) {
        this.name = name;
        validateName();
    }

    private void validateName() {
        if (name == null || name.isBlank() || name.length() > MAX_NAME_LENGTH) {
            throw new InvalidArgumentException("이름은 null이거나 비어있을 수 없으며, 최대 " + MAX_NAME_LENGTH + "자를 초과할 수 없습니다.");
        }
    }
}
