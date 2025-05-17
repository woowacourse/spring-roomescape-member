package roomescape.domain;

import java.util.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReservationTheme {

    private static final int THEME_NAME_MAX_SIZE = 20;

    @EqualsAndHashCode.Include
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public ReservationTheme(final Long id, final String name, final String description, final String thumbnail) {
        validateNotNulls(name, description, thumbnail);
        validateThemeNameSize(name);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public ReservationTheme(final String name, final String description, final String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public ReservationTheme assignId(final Long id) {
        return new ReservationTheme(id, name, description, thumbnail);
    }

    private void validateNotNulls(String name, String description, String thumbnail) {
        Objects.requireNonNull(name, "테마 이름을 입력 해 주세요.");
        Objects.requireNonNull(description, "설명을 입력 해 주세요.");
        Objects.requireNonNull(thumbnail, "썸네일을 입력 해 주세요.");
    }

    private void validateThemeNameSize(String name) {
        if (name.length() > THEME_NAME_MAX_SIZE) {
            throw new IllegalArgumentException(
                    "[ERROR] 테마명은 " + THEME_NAME_MAX_SIZE + "글자 이내로 입력해 주세요. 현재 길이는 " + name.length() + "글자 입니다.");
        }
    }
}
