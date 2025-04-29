package roomescape.theme.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.theme.application.converter.ThemeConverter;
import roomescape.theme.application.dto.CreateThemeServiceRequest;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeId;
import roomescape.theme.domain.ThemeRepository;

@RequiredArgsConstructor
@Service
public class ThemeCommandUseCaseImpl implements ThemeCommandUseCase {

    private final ThemeRepository themeRepository;

    @Override
    public Theme create(final CreateThemeServiceRequest createThemeServiceRequest) {
        return themeRepository.save(ThemeConverter.toDomain(createThemeServiceRequest));
    }

    @Override
    public void delete(final ThemeId id) {
        themeRepository.deleteById(id);
    }
}
