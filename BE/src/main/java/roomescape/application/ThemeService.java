package roomescape.application;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.PopularThemeRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.domain.ThemeSortType;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final PopularThemeRepository popularThemeRepository;

    public ThemeService(ThemeRepository themeRepository, PopularThemeRepository popularThemeRepository) {
        this.themeRepository = themeRepository;
        this.popularThemeRepository = popularThemeRepository;
    }

    public Theme save(String name, String description, String thumbnailUrl) {
        return themeRepository.save(Theme.create(name, description, thumbnailUrl));
    }

    public Optional<Theme> findById(Long id) {
        return themeRepository.findById(id);
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public List<Theme> findTopNByPeriod(LocalDate startAt, LocalDate endAt, String sortBy, Long limit) {
        ThemeSortType sortType = ThemeSortType.valueOf(sortBy.toUpperCase());
        return popularThemeRepository.findTopNByPeriod(startAt, endAt, sortType, limit);
    }

    public void deleteById(Long id) {
        themeRepository.deleteById(id);
    }
}
