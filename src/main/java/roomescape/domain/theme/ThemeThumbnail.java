package roomescape.domain.theme;

import java.util.Objects;

public class ThemeThumbnail {

    private final String value;

    private ThemeThumbnail(String value) {
        this.value = value;
    }

    public static ThemeThumbnail from(String value) {
        validateNull(value);
        return new ThemeThumbnail(value);
    }

    private static void validateNull(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("테마 썸네일은 비어있을 수 없습니다.");
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
