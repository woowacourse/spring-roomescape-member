package roomescape.domain.reservation;

import java.util.Objects;

public class ThemeThumbnail {

    private final String value;

    public ThemeThumbnail(String value) {
        this.value = value;
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
        ThemeThumbnail that = (ThemeThumbnail) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
