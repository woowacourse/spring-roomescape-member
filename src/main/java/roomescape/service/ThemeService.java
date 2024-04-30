package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ThemeDao;
import roomescape.domain.theme.Theme;
import roomescape.dto.theme.ThemeCreateRequest;
import roomescape.dto.theme.ThemeResponse;

@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<ThemeResponse> findAll() {
        return themeDao.readAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse add(ThemeCreateRequest request) {
        Theme theme = request.toDomain();
        Theme result = themeDao.create(theme);
        return ThemeResponse.from(result);
    }
}
