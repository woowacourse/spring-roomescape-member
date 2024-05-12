package roomescape.reservation.domain;

import java.util.Objects;

public class Thumbnail {
    private final String value;

    public Thumbnail(String value) {
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
        Thumbnail that = (Thumbnail) o;
        return Objects.equals(value, that.value);
    }

    public String getValue() {
        return value;
    }
}
