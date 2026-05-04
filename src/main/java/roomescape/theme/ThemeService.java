package roomescape.theme;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse create(ThemeRequest themeRequest) {
        Theme theme = new Theme(
                themeRequest.name(),
                themeRequest.description(),
                themeRequest.thumbnail()
        );

        Theme saved = themeRepository.save(theme);
        return ThemeResponse.from(saved);
    }

    public List<ThemeResponse> read() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public void delete(Long id) {
        themeRepository.deleteById(id);
    }


    public List<ThemeResponse> readPopularThemes() {
        LocalDate now = LocalDate.now();
        LocalDate start = now.minusDays(7);
        LocalDate end = now.minusDays(1);

        List<Theme> themes = themeRepository.findPopularThemes(start, end);
        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
