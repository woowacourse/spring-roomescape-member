package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> getThemes() {
        return themeRepository.findAll();
    }

    @Transactional
    public Theme addTheme(Theme theme) {
        Long id = themeRepository.save(theme);
        return findById(id);
    }

    @Transactional
    public void deleteTheme(Long id) {
        themeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Theme findById(Long id) {
        return themeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마 ID입니다."));
    }
}
