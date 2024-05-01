package roomescape.domain.theme;

import java.util.Objects;

public class ThemeThumbnail {

    private final String value;

    private ThemeThumbnail(String value) {
        this.value = value;
    }

    public static ThemeThumbnail from(String value) {
        return new ThemeThumbnail(value);
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
        ThemeThumbnail other = (ThemeThumbnail) o;
        return Objects.equals(this.value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "ThemeThumbnail{" +
                "value='" + value + '\'' +
                '}';
    }
}
