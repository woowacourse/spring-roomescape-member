package roomescape.domain;

import lombok.Getter;

@Getter
public class ThemeName {

    private final String name;

    private ThemeName(final String value) {
        this.name = value;
    }

    public static ThemeName from(String value) {
        return new ThemeName(value);
    }

}
