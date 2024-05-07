package roomescape.domain;

import roomescape.exception.InvalidInputException;

public class Name {
    private final String name;

    public Name(String name) {
        validateEmpty(name);
        this.name = name;
    }

    public void validateEmpty(String name) {
        if (name.isBlank()) {
            throw new InvalidInputException("이름에 공백을 입력할 수 없습니다.");
        }
    }

    public String getName() {
        return name;
    }
}
