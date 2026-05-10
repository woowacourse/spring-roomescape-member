package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private static final int POPULAR_STATISTICS_DAYS = 7;
    private static final int PREVIOUS_DAYS = 1;

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme readTheme(Long id) {
        return getTheme(id);
    }

    public List<Theme> readThemes() {
        return themeRepository.findAll();
    }

    public List<Theme> readActiveThemes() {
        return themeRepository.findByIsActive(true);
    }

    public List<Theme> readPopularThemes(int top) {
        LocalDate endDate = LocalDate.now().minusDays(PREVIOUS_DAYS);
        LocalDate startDate = endDate.minusDays(POPULAR_STATISTICS_DAYS);
        return themeRepository.findPopularThemes(startDate, endDate, top);
    }

    @Transactional
    public Theme register(String name, String description, String thumbnailUrl) {
        return themeRepository.save(Theme.create(name, description, thumbnailUrl));
    }

    @Transactional
    public Theme updateStatus(Long id, boolean isActive) {
        Theme theme = getTheme(id);
        theme.updateStatus(isActive);
        themeRepository.updateStatus(theme);
        if (!themeRepository.updateStatus(theme)) {
            throw new IllegalArgumentException("해당 테마가 존재하지 않습니다.");
        }
        return theme;
    }

    private Theme getTheme(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 테마가 존재하지 않습니다."));
    }

}
