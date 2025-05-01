package roomescape.test.fixture;

import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

public class ThemeFixture {

    public static Theme addThemeInRepository(ThemeRepository repository, String name, String description,
            String thumbnail) {
        Theme theme = Theme.createWithoutId(name, description, thumbnail);
        long themeId = repository.addTheme(theme);
        return repository.findById(themeId).get();
    }
}
