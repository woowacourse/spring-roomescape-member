package roomescape.theme.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;
import roomescape.theme.ui.dto.ThemeResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeQueryUseCaseImpl implements ThemeQueryUseCase {

    private final ThemeRepository themeRepository;

    @Override
    public List<Theme> getAll() {
        return themeRepository.findAll();
    }
}
