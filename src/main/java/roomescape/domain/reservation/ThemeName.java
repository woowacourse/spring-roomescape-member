package roomescape.domain.reservation;

import java.util.Objects;

public class ThemeName {

    private final String value;

    public ThemeName(String value) {
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
        ThemeName themeName = (ThemeName) o;
        return Objects.equals(value, themeName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
