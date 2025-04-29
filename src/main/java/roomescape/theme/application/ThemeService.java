package roomescape.theme.application;

import roomescape.theme.domain.ThemeId;
import roomescape.theme.ui.dto.CreateThemeWebRequest;
import roomescape.theme.ui.dto.ThemeResponse;

import java.util.List;

public interface ThemeService {

    List<ThemeResponse> getAll();

    ThemeResponse create(CreateThemeWebRequest createThemeWebRequest);

    void delete(ThemeId id);
}
