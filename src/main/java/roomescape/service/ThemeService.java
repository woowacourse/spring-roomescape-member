package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.entity.Theme;
import roomescape.exception.impl.ThemeNotFoundException;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {

    private static final int AGGREGATE_START_DATE_INTERVAL = 7;
    private static final int AGGREGATE_END_DATE_INTERVAL = 1;
    private static final int AGGREGATE_COUNT = 10;
    
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
        LocalDate now = LocalDate.now();
        return themeRepository.findPopularThemes(
                now.minusDays(AGGREGATE_START_DATE_INTERVAL),
                now.minusDays(AGGREGATE_END_DATE_INTERVAL),
                AGGREGATE_COUNT
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
