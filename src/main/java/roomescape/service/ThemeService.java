package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.domain.dto.ThemeRequest;
import roomescape.domain.dto.ThemeResponse;
import roomescape.repository.ThemeDao;

import java.util.List;

@Service
public class ThemeService {
    private final ThemeDao themeDao;

    public ThemeService(final ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<ThemeResponse> findAll() {
        return themeDao.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse findById(final Long id) {
        Theme theme = themeDao.findById(id);
        return ThemeResponse.from(theme);
    }

    public ThemeResponse create(final ThemeRequest themeRequest) {
        Long id = themeDao.create(themeRequest);
        Theme theme = themeRequest.toEntity(id);
        return ThemeResponse.from(theme);
    }

    public void delete(final Long id) {
        themeDao.delete(id);
    }
}
