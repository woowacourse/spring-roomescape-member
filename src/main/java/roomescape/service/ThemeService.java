package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.response.ThemeResponse;
import roomescape.dao.ThemeDAO;
import roomescape.domain.Theme;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.NotExistedValueException;
import roomescape.service.dto.ThemeCreation;

@Service
public class ThemeService {

    private final ThemeDAO themeDAO;

    public ThemeService(final ThemeDAO themeDAO) {
        this.themeDAO = themeDAO;
    }

    public ThemeResponse addTheme(final ThemeCreation themeCreation) {
        if (themeDAO.existsByName(themeCreation.name())) {
            throw new ExistedDuplicateValueException("이미 존재하는 테마입니다");
        }

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

    public void deleteTheme(final long id) {
        boolean deleted = themeDAO.deleteById(id);

        if (!deleted) {
            throw new NotExistedValueException("존재하지 않는 테마입니다");
        }
    }
}
