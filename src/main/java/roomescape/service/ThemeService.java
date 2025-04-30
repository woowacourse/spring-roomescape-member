package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.entity.Theme;
import roomescape.exception.impl.ThemeNotFoundException;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme add(final String name, final String description, final String thumbnail) {
        Theme theme = Theme.beforeSave(name, description, thumbnail);
        return themeRepository.save(theme);
    }

    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }

    public List<Theme> getPopularThemes() {
        return themeRepository.findPopularThemes(
                LocalDate.now().minusDays(7),
                LocalDate.now().minusDays(1),
                10
        );
    }

    public void deleteById(final long id) {
        boolean exist = themeRepository.existById(id);
        if (!exist) {
            throw new ThemeNotFoundException();
        }
        themeRepository.deleteById(id);
    }
}
