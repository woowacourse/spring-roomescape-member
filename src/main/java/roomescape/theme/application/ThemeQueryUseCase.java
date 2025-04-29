package roomescape.theme.application;

import roomescape.theme.domain.Theme;
import roomescape.theme.ui.dto.ThemeResponse;

import java.util.List;

public interface ThemeQueryUseCase {

    List<Theme> getAll();
}
