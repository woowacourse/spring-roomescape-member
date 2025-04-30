package roomescape.theme.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.theme.application.converter.ThemeConverter;
import roomescape.theme.application.usecase.ThemeCommandUseCase;
import roomescape.theme.application.usecase.ThemeQueryUseCase;
import roomescape.theme.domain.ThemeId;
import roomescape.theme.ui.dto.CreateThemeWebRequest;
import roomescape.theme.ui.dto.ThemeResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeServiceImpl implements ThemeService {

    private final ThemeQueryUseCase themeQueryUseCase;
    private final ThemeCommandUseCase themeCommandUseCase;

    @Override
    public List<ThemeResponse> getAll() {
        return ThemeConverter.toDto(themeQueryUseCase.getAll());
    }

    @Override
    public ThemeResponse create(final CreateThemeWebRequest createThemeWebRequest) {
        return ThemeConverter.toDto(themeCommandUseCase.create(ThemeConverter.toServiceDto(createThemeWebRequest)));
    }

    @Override
    public void delete(final ThemeId id) {
        themeCommandUseCase.delete(id);
    }
}
