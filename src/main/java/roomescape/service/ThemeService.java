package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ThemeResponse;

import java.util.List;

@Service
public interface ThemeService {

    ThemeResponse save(final ThemeRequest themeRequest);

    List<ThemeResponse> getAll();

    List<ThemeResponse> findThemeRanking();

    void delete(final long id);
}
