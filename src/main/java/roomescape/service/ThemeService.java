package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.ThemeAddRequest;
import roomescape.dto.ThemeResponse;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> findAllTheme() {
        return themeRepository.findAll();
    }

    public List<ThemeResponse> findPopularTheme() {
        return themeRepository.findTopOrderByReservationCount().stream()
                .map(ThemeResponse::new)
                .toList();
    }

    public Theme saveTheme(ThemeAddRequest themeAddRequest) {
        Theme theme = themeAddRequest.toEntity();
        return themeRepository.save(theme);
    }

    public void removeTheme(Long id) {
        themeRepository.deleteById(id);
    }
}
