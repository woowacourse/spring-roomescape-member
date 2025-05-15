package roomescape.integration.service.stub;

import java.util.List;
import roomescape.domain.Theme;
import roomescape.service.ThemeService;

public class StubThemeService extends ThemeService {
    private final Theme testTheme = new Theme(1L, "hippo", "test description", "test.jpg");

    public StubThemeService() {
        super(null);
    }

    @Override
    public Theme saveTheme(Theme theme) {
        return testTheme;
    }

    @Override
    public List<Theme> readTheme() {
        return List.of(testTheme);
    }

    @Override
    public void deleteTheme(Long id) {}

    @Override
    public List<Theme> readLists(String orderType, Long listNum) {
        return List.of(testTheme);
    }
}
