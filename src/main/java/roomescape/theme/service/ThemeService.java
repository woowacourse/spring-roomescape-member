package roomescape.theme.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.entity.Theme;
import roomescape.theme.payload.ThemeRequest;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Theme save(ThemeRequest request) {
        Theme theme = Theme.create(
                request.name(),
                request.description(),
                request.thumbnailUrl(),
                Theme.RUNTIME
        );

        return themeRepository.save(theme);
    }

    @Transactional(readOnly = true)
    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Theme> findPopularThemes(int days, int limits) {
        return themeRepository.findPopularThemes(days, limits);
    }

    @Transactional
    public void deleteById(Long id) {
        themeRepository.deleteById(id);
    }

}
