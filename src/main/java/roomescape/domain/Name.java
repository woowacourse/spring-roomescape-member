package roomescape.domain;

import java.util.Objects;

public class Name {

    private final String name;

    public Name(String name) {
        validateNonBlank(name);
        this.name = name;
    }

    private void validateNonBlank(String name) {
        if (name == null || name.isEmpty()) {
            throw new NullPointerException("이름은 비어있을 수 없습니다.");
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Name name1 = (Name) o;
        return Objects.equals(name, name1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
