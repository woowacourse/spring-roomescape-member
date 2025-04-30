package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.exception.ResourceNotExistException;

@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<ThemeResponse> getAll() {
        List<Theme> themes = themeDao.findAll();
        return themes.stream()
            .map(ThemeResponse::from)
            .toList();
    }

    public ThemeResponse save(ThemeRequest request) {
        Theme theme = new Theme(
            request.name(),
            request.description(),
            request.thumbnail()
        );
        return ThemeResponse.from(themeDao.save(theme));
    }

    public void deleteById(Long id) {
        int count = themeDao.deleteById(id);
        if (count == 0) {
            throw new ResourceNotExistException();
        }
    }
}
