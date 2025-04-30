package roomescape.application;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.application.dto.ThemeDto;
import roomescape.application.mapper.ThemeMapper;
import roomescape.domain.Theme;
import roomescape.domain.repository.ThemeRepository;
import roomescape.exception.NotFoundException;
import roomescape.presentation.dto.request.ThemeRequest;
import roomescape.presentation.dto.response.AdminThemeResponse;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<ThemeDto> getAllThemes() {
        List<Theme> themes = themeRepository.findAll();
        return ThemeMapper.toDtos(themes);
    }

    public ThemeDto registerTheme(@Valid ThemeRequest themeRequest) {
        Theme themeWithoutId = ThemeMapper.toDomain(themeRequest);
        Long id = themeRepository.save(themeWithoutId);
        Theme theme = Theme.assignId(id, themeWithoutId);
        return ThemeMapper.toDto(theme);
    }

    public void deleteTheme(Long id) {
        themeRepository.deleteById(id);
    }

    public Theme getThemeById(@NotNull Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 테마가 없습니다."));
    }
}
