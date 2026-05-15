package roomescape.domain;

import lombok.Getter;

@Getter
public class ThemeName {

    private final String name;

    public ThemeName(final String value) {
        validate(value);
        this.name = value;
    }

    private void validate(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("테마 이름은 비워둘 수 없습니다.");
        }
    }
}
