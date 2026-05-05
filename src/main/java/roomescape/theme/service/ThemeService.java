package roomescape.theme.service;

import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Transactional(readOnly = true)
    public List<Theme> readThemes() {
        return themeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Theme readTheme(Long id) {
        return getTheme(id);
    }

    @Transactional(readOnly = true)
    public List<Theme> readActiveThemes() {
        return themeRepository.findByStatus(true);
    }

    @Transactional
    public Theme register(String name, String description, String thumbnailUrl) {
        return themeRepository.save(Theme.create(name, description, thumbnailUrl));
    }

    @Transactional
    public Theme updateStatus(Long id, boolean isActive) {
        Theme theme = getTheme(id);
        theme.updateStatus(isActive);
        if (!themeRepository.updateStatus(theme)) {
            throw new IllegalArgumentException("해당 테마가 존재하지 않습니다.");
        }
        return theme;
    }

    @NonNull
    private Theme getTheme(Long id) {
        return themeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 테마가 존재하지 않습니다."));
    }
}
