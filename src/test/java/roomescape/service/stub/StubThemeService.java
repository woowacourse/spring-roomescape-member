package roomescape.service.stub;

import java.util.List;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeCreateRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ThemeService;

public class StubThemeService extends ThemeService {
    private final Theme testTheme = new Theme(1L, "hippo", "test description", "test.jpg");

    public StubThemeService() {
        super(null);
    }

    @Override
    public ThemeResponse createTheme(ThemeCreateRequest request) {
        return ThemeResponse.from(testTheme);
    }

    @Override
    public List<ThemeResponse> findAll() {
        return List.of(ThemeResponse.from(testTheme));
    }

    @Override
    public void deleteThemeById(Long id) {}

    @Override
    public List<ThemeResponse> findLimitedThemesByPopularDesc(String orderType, Long listNum) {
        return List.of(ThemeResponse.from(testTheme));
    }
}
