package roomescape.domain.fixture;

import roomescape.domain.Theme;

public final class ThemeFixture {

    private ThemeFixture() {
    }

    public static Theme createDefaultTheme() {
        return Theme.create("공포테마", "어마무시한 공포 테마입니다.", "https://image.com/image.png");
    }

    public static Theme createThemeWithId() {
        return Theme.restore(1L, "공포테마", "어마무시한 공포 테마입니다.", "https://image.com/image.png", true);
    }

    public static Theme createTheme(String name, String description, String thumbnailImageUrl) {
        return Theme.create(name, description, thumbnailImageUrl);
    }
}
