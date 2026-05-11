package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

@Slf4j
@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Transactional(readOnly = true)
    public List<Theme> findThemes() {
        return themeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Theme findTheme(Long id) {
        return getTheme(id);
    }

    @Transactional(readOnly = true)
    public List<Theme> findActiveThemes() {
        return themeRepository.findByStatus(true);
    }

    @Transactional(readOnly = true)
    public List<Theme> findPopularThemes(int top) {
        LocalDate today = LocalDate.now();

        LocalDate startDate = today.minusDays(7);
        LocalDate endDate = today;

        return themeRepository.findPopularThemes(startDate, endDate, top);
    }

    @Transactional
    public Theme register(String name, String description, String thumbnailUrl) {
        Theme theme = themeRepository.save(Theme.create(name, description, thumbnailUrl));
        log.info("Theme registered: id={}, name={}", theme.id(), theme.name());
        return theme;
    }

    @Transactional
    public Theme updateStatus(Long id, boolean isActive) {
        Theme theme = getTheme(id);
        theme.updateStatus(isActive);
        if (!themeRepository.update(theme)) {
            log.warn("Theme status update failed: id={}, name={}", theme.id(), theme.name());
            throw new IllegalArgumentException("테마 상태 변경에 실패했습니다.");
        }
        log.info("Theme status updated: id={}, name={}, isActive={}", theme.id(), theme.name(), theme.isActive());
        return theme;
    }

    @NonNull
    private Theme getTheme(Long id) {
        return themeRepository.findById(id).orElseThrow(() -> {
            log.warn("Theme not found: id={}", id);
            return new IllegalArgumentException("해당 테마가 존재하지 않습니다.");
        });
    }
}
