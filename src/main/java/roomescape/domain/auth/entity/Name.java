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
        if (name == null || name.isBlank()) {
            throw new InvalidArgumentException("이름은 null이거나 비어있을 수 없습니다 ");
        }

        if (name.length() > MAX_NAME_LENGTH) {
            throw new InvalidArgumentException("이름은 " + MAX_NAME_LENGTH + "자 이하로 입력해야 합니다");
        }
    }
}
