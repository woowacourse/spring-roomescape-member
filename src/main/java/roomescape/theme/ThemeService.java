package roomescape.theme;

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

// TODO
//    public List<ThemeResponse> readPopularThemes() {
//
//    }
}
