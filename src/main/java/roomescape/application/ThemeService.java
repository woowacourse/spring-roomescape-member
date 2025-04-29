package roomescape.application;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.application.mapper.ThemeMapper;
import roomescape.domain.Theme;
import roomescape.domain.repository.ThemeRepository;
import roomescape.presentation.dto.response.ThemeResponse;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<ThemeResponse> getAllThemes() {
        List<Theme> themes = themeRepository.findAll();
        return ThemeMapper.toDtos(themes);
    }
}
