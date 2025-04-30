package roomescape.test.fixture;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

public class ThemeFixture {
    public static Theme addThemeInRepository(ThemeRepository repository, String name, String description, String thumbnail) {
        Theme theme = Theme.createWithoutId(name, description, thumbnail);
        long themeId = repository.addTheme(theme);
        return repository.findById(themeId).get();
    }
}
