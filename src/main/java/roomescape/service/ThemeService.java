package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> allTheme() {
        return themeRepository.findAll();
    }

    public Theme saveTheme(String name, String description, String thumbnailUrl) {
        Theme theme = new Theme(name, description, thumbnailUrl);
        return themeRepository.save(theme);
    }

    public void removeTheme(long timeId) {
        themeRepository.deleteById(timeId);
    }

    public Theme findTheme(long timeId) {
        return themeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("타임 ID가 존재하지 않습니다."));
    }

    public List<Theme> findPopularThemes(Long topCount, Long during) {
        LocalDate fromDate = LocalDate.now().minusDays(during);
        LocalDate toDate = LocalDate.now();
        return themeRepository.findPopularThemes(topCount, fromDate, toDate);
    }
}
