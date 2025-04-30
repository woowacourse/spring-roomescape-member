package roomescape.theme.application.usecase;

import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeId;

import java.util.List;

public interface ThemeQueryUseCase {

    List<Theme> getAll();

    Theme get(ThemeId id);
}
