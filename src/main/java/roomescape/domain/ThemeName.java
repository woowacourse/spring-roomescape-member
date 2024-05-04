package roomescape.domain;

import java.util.Objects;

public record ThemeName(String value) {
    public ThemeName {
        Objects.requireNonNull(value);
        if (value.isEmpty()) {
            throw new IllegalArgumentException("테마 이름은 한글자 이상이어야 합니다.");
        }
    }
}
