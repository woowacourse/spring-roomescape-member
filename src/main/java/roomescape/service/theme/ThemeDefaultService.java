package roomescape.service.theme;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.theme.ThemeRequest;
import roomescape.dto.theme.ThemeResponse;
import roomescape.exception.theme.ThemeNotFoundException;
import roomescape.repository.theme.ThemeRepository;

@Service
public class ThemeDefaultService implements ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeDefaultService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Override
    public ThemeResponse create(ThemeRequest request) {
        Theme theme = Theme.createWithoutId(request.name(), request.description(), request.thumbnail());
        return ThemeResponse.from(themeRepository.add(theme));
    }

    @Override
    public List<ThemeResponse> getAll() {
        List<Theme> themes = themeRepository.findAll();
        return ThemeResponse.from(themes);
    }

    @Override
    public void deleteById(Long id) {
        int affectedCount = themeRepository.deleteById(id);
        if (affectedCount == 0) {
            throw new ThemeNotFoundException();
        }
    }
}
