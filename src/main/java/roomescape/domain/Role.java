package roomescape.domain;

import java.util.Objects;

public class Role {
    private final String value;

    public Role(String value) {
        validateRole(value);
        this.value = value;
    }

    private void validateRole(String value) {
        if (!value.equals("ADMIN") && !value.isEmpty()) {
            throw new IllegalArgumentException("잘못된 role 입니다.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Role role = (Role) o;
        return Objects.equals(value, role.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
