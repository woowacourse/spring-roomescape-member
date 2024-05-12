package roomescape.reservation.domain;

import java.util.Objects;

public class Description {
    private final String value;

    public Description(String value) {
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
        Description that = (Description) o;
        return Objects.equals(value, that.value);
    }

    public String getValue() {
        return value;
    }
}
