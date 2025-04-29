package roomescape.theme.application;

import roomescape.theme.domain.Theme;

import java.util.List;

public interface ThemeQueryUseCase {

    List<Theme> getAll();
}
