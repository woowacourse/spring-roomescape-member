package roomescape.theme;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.exception.DuplicateException;

@Service
public class AdminThemeService {

    private final ThemeRepository themeRepository;

    public AdminThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme save(String name, String description, String thumbnail) {
        try {
            return themeRepository.save(name, description, thumbnail);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateException("같은 이름의 테마가 존재합니다.");
        }

    }

    public void delete(long id) {
        themeRepository.delete(id);
    }
}
