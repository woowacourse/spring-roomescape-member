package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.repository.ThemeDao;

@Service
public class ThemeService {
    private final ThemeDao themeDao;

    public ThemeService(final ThemeDao themeDao) {
        this.themeDao = themeDao;
    }


    public ThemeResponse save(final ThemeRequest themeRequest) {
        Theme theme = themeRequest.toEntity();
        return ThemeResponse.from(themeDao.save(theme));
    }

    public List<ThemeResponse> getAll() {
        return themeDao.getAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
