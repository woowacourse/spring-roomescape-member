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
    public Theme save(ThemeCreateRequest request) {
        return testTheme;
    }

    @Override
    public List<Theme> read() {
        return List.of(testTheme);
    }

    @Override
    public void delete(Long id) {}

    @Override
    public List<Theme> readLists(String orderType, Long listNum) {
        return List.of(testTheme);
    }
}
