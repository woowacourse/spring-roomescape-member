package roomescape.theme;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class AdminThemeService {

    private final ThemeRepository themeRepository;

    public AdminThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public long save(String name, String description, String thumbnail) {
        try {
            return themeRepository.save(name, description, thumbnail);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateThemeException("같은 이름의 테마가 존재합니다.");
        }

    }

    public void delete(long id) {
        themeRepository.delete(id);
    }
}
