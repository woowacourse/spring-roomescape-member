package roomescape.theme.service;

import roomescape.theme.controller.dto.CreateThemeWebRequest;
import roomescape.theme.controller.dto.ThemeResponse;

import java.util.List;

public interface ThemeService {

    List<ThemeResponse> getAll();

    List<ThemeResponse> getRanking();

    ThemeResponse create(CreateThemeWebRequest createThemeWebRequest);

    void delete(Long id);
}
