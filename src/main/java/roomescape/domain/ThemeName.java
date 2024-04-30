package roomescape.domain;

import java.util.Objects;
import roomescape.exception.InvalidReservationException;

public class ThemeName {
    private final String value;

    public ThemeName(String value) {
        String trimmedValue = value.trim();
        validate(trimmedValue);
        this.value = value;
    }

    private void validate(String value) {
        if (value.isEmpty()) {
            throw new InvalidReservationException("이름은 빈칸(공백)일 수 없습니다.");
        }
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
        return Objects.hash(value);
    }
}
