package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.response.ThemeResponse;
import roomescape.dao.ThemeDAO;
import roomescape.domain.Theme;
import roomescape.service.dto.ThemeCreation;

@Service
public class ThemeService {

    private final ThemeDAO themeDAO;

    public ThemeService(final ThemeDAO themeDAO) {
        this.themeDAO = themeDAO;
    }

    public ThemeResponse addTheme(final ThemeCreation themeCreation) {
        Theme theme = new Theme(themeCreation.name(), themeCreation.description(), themeCreation.thumbnail());
        long id = themeDAO.insert(theme);
        return ThemeResponse.from(theme.withId(id));
    }

    public List<ThemeResponse> findAllThemes() {
        return themeDAO.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public boolean deleteTheme(final long id) {
        return themeDAO.deleteById(id);
    }
}
