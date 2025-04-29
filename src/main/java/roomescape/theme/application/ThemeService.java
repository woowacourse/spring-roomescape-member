package roomescape.theme.application;

import roomescape.theme.ui.dto.ThemeResponse;

import java.util.List;

public interface ThemeService {

    List<ThemeResponse> getAll();
}
