package roomescape.fixture;

import roomescape.domain.theme.Theme;

public class ThemeFixture {
    public static Theme theme() {
        return theme(1L);
    }

    public static Theme theme(long id) {
        return theme(id, "테마1", "설명", "링크");
    }

    public static Theme theme(String name) {
        return theme(1L, name, "설명", "링크");
    }

    public static Theme theme(long id, String name, String description, String thumbnail) {
        return new Theme(id, name, description, thumbnail);
    }
}
