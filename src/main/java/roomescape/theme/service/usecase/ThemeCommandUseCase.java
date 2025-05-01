package roomescape.theme.service.usecase;

import roomescape.theme.service.dto.CreateThemeServiceRequest;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeId;

public interface ThemeCommandUseCase {

    Theme create(CreateThemeServiceRequest createThemeServiceRequest);

    void delete(ThemeId id);
}
