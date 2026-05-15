package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.exception.DeletionNotAllowedException;
import roomescape.repository.ThemeDao;

@Service
@RequiredArgsConstructor
public class ThemeCommandService {

    private final ThemeDao themeDao;

    public Theme create(String name, String thumbnailUrl, String description) {
        return themeDao.save(name, thumbnailUrl, description);
    }

    public void delete(long themeId) {
        try {
            themeDao.delete(themeId);
        } catch (DataIntegrityViolationException e) {
            throw new DeletionNotAllowedException();
        }
    }
}
