package roomescape.domain;

import roomescape.exception.InvalidInputException;

public class Name {
    private final String name;

    public Name(String name) {
        validateBlank(name);
        this.name = name;
    }

    public void validateBlank(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidInputException("사용자 이름에 공백을 입력할 수 없습니다.");
        }
    }

    public String getName() {
        return name;
    }
}
