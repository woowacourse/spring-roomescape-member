package roomescape.application;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.entity.Theme;
import roomescape.entity.ThemeRepository;
import roomescape.entity.ThemeSortType;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme save(String name, String description, String thumbnailUrl) {
        return themeRepository.save(Theme.createWithNullId(name, description, thumbnailUrl));
    }

    public Optional<Theme> findById(Long id) {
        return themeRepository.findById(id);
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public List<Theme> findTopNByPeriod(LocalDate startAt, LocalDate endAt, ThemeSortType sortType, Long limit) {
        return themeRepository.findTopNByPeriod(startAt, endAt, sortType, limit);
    }

    public void deleteById(Long id) {
        themeRepository.deleteById(id);
    }
}
