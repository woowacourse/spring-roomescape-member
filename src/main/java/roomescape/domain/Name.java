package roomescape.domain;

import java.util.Objects;

public class Name {
    private final String name;

    public Name(String name) {
        validateNonBlank(name);
        this.name = name;
    }

    private void validateNonBlank(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("예약자명은 필수 입력값 입니다.");
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Name name)) {
            return false;
        }
        return Objects.equals(this.name, name.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
