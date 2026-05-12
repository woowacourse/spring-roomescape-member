package roomescape.domain;

import lombok.Getter;

@Getter
public class ThemeName {

    private final String name;

    public ThemeName(final String value) {
        this.name = value;
    }
}
