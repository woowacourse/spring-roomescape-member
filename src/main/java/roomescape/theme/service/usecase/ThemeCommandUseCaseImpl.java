package roomescape.theme.service.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.theme.service.converter.ThemeConverter;
import roomescape.theme.service.dto.CreateThemeServiceRequest;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeId;
import roomescape.theme.repository.ThemeRepository;

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
