package roomescape.theme.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ThemeDefaultService {
    private final ThemeRepository themeRepository;

    public ThemeDefaultService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse create(ThemeRequest request) {
        Theme theme = Theme.createWithoutId(request.name(), request.description(), request.thumbnail());
        return ThemeResponse.from(themeRepository.add(theme));
    }

    public List<ThemeResponse> getAll() {
        List<Theme> themes = themeRepository.findAll();
        return ThemeResponse.from(themes);
    }

    public void deleteById(Long id) {
        int affectedCount = themeRepository.deleteById(id);
        if (affectedCount == 0) {
            throw new ThemeNotFoundException();
        }
    }
}
