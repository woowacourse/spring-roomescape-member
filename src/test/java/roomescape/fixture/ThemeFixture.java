package roomescape.fixture;

import roomescape.domain.theme.Theme;

public class ThemeFixture {
    public static Theme theme() {
        return theme(1L);
    }

    public static Theme theme(long id) {
        return theme(id, "테마 이름", "설명 내용", "http://test.jpg");
    }

    public static Theme theme(long id, String name, String description, String thumbnail) {
        return new Theme(id, name, description, thumbnail);
    }
}
