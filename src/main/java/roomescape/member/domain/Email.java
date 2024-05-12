package roomescape.member.domain;

import java.util.Objects;

public class Email {
    private final String value;

    public Email(String value) {
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
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }

    public String getValue() {
        return value;
    }
}
