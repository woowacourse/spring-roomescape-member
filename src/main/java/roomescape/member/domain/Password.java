package roomescape.member.domain;

import java.util.Objects;

public class Password {
    private final String value;

    public Password(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Objects.equals(value, password.value);
    }

    public String getValue() {
        return value;
    }
}
