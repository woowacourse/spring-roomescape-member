package roomescape.theme.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.exception.ExistedThemeException;
import roomescape.theme.Theme;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.dto.request.ThemeRequest;
import roomescape.theme.dto.response.ThemeResponse;

@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public ThemeResponse create(ThemeRequest themeRequest) {
        Optional<Theme> optionalTheme = themeDao.findByName(themeRequest.name());
        if (optionalTheme.isPresent()) {
            throw new ExistedThemeException();
        }

        Theme theme = Theme.createWithoutId(themeRequest.name(), themeRequest.description(), themeRequest.thumbnail());
        Theme themeWithId = themeDao.create(theme);
        return new ThemeResponse(themeWithId.getId(), themeWithId.getName(), themeWithId.getDescription(),
                themeWithId.getThumbnail());
    }

    public List<ThemeResponse> findAll() {
        List<Theme> themes = themeDao.findAll();
        return themes.stream()
                .map(theme ->
                        new ThemeResponse(
                                theme.getId(),
                                theme.getName(),
                                theme.getDescription(),
                                theme.getThumbnail()
                        )
                )
                .toList();
    }

    public void delete(long id) {
        themeDao.delete(id);
    }
}
