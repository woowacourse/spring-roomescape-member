package roomescape.member.domain;

import java.util.Objects;

public class Name {

    private final String name;

    public Name(String name) {
        name = Objects.requireNonNull(name);
        validateNameLength(name);

        this.name = name;
    }

    private static void validateNameLength(String name) {
        if (name.length() <= 0 || name.length() > 5) {
            throw new IllegalArgumentException("이름은 0-5글자 사이여야 합니다.");
        }
    }

    public String getName() {
        return name;
    }
}
