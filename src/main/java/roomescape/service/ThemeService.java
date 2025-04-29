package roomescape.service;

import java.util.List;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;

public interface ThemeService {
    ThemeResponse create(ThemeRequest request);

    List<ThemeResponse> getAll();

    void deleteById(Long id);
}
