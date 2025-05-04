package roomescape.service.stub;

import java.util.List;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeCreateRequest;
import roomescape.service.ThemeService;

public class StubThemeService extends ThemeService {
    private final Theme testTheme = new Theme(1L, "hippo", "test description", "test.jpg");

    public StubThemeService() {
        super(null);
    }

    @Override
    public Theme createTheme(ThemeCreateRequest request) {
        return testTheme;
    }

    @Override
    public List<Theme> findAll() {
        return List.of(testTheme);
    }

    @Override
    public void deleteThemeById(Long id) {}

    @Override
    public List<Theme> findLimitedThemesByPopularDesc(String orderType, Long listNum) {
        return List.of(testTheme);
    }
}
