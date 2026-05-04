package roomescape.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.dto.response.ThemeResponse;
import roomescape.repository.ThemeDao;

@Service
@RequiredArgsConstructor
public class ThemeQueryService {
    private final ThemeDao themeDao;

    public List<ThemeResponse> findAllThemes() {
        return themeDao.findAllThemes().stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
