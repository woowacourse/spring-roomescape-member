package roomescape.theme.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.theme.application.converter.ThemeConverter;
import roomescape.theme.ui.dto.ThemeResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeServiceImpl implements ThemeService {

    private final ThemeQueryUseCase themeQueryUseCase;

    @Override
    public List<ThemeResponse> getAll() {
        return ThemeConverter.toDto(themeQueryUseCase.getAll());
    }
}
