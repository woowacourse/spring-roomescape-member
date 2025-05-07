package roomescape.theme.service;

import java.util.List;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;

public interface ThemeService {
    ThemeResponse create(ThemeRequest request);

    List<ThemeResponse> getAll();

    void deleteById(Long id);
}
