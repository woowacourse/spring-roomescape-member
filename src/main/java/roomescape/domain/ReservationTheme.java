package roomescape.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReservationTheme {

    private static final int THEME_NAME_MAX_SIZE = 20;

    @EqualsAndHashCode.Include
    private Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public ReservationTheme(final String name, final String description, final String thumbnail) {
        validateThemeNameSize(name);
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public ReservationTheme assignId(final Long id) {
        this.id = id;
        return this;
    }

    private void validateThemeNameSize(String name) {
        if (name.length() > THEME_NAME_MAX_SIZE) {
            throw new IllegalArgumentException(
                    "[ERROR] 테마명은 " + THEME_NAME_MAX_SIZE + "글자 이내로 입력해 주세요. 현재 길이는 " + name.length() + "글자 입니다.");
        }
    }
}
