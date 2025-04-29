package roomescape.theme.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.theme.controller.request.ThemeCreateRequest;
import roomescape.theme.controller.response.ThemeResponse;
import roomescape.theme.domain.Theme;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public void deleteById(Long id) {
        themeRepository.deleteById(id);
    }

    public ThemeResponse create(ThemeCreateRequest request) {
        Theme theme = themeRepository.save(request.name(), request.description(), request.thumbnail());
        return ThemeResponse.from(theme);
    }

    public List<ThemeResponse> getAll() {
        List<Theme> themes = themeRepository.findAll();
        return ThemeResponse.from(themes);
    }

    public Theme getTheme(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 해당 테마가 존재하지 않습니다."));
    }
}
