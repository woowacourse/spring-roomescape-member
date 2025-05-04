package roomescape.theme.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.application.dto.CreateThemeServiceRequest;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeId;
import roomescape.theme.domain.ThemeRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class ThemeCommandUseCaseImpl implements ThemeCommandUseCase {

    private final ThemeRepository themeRepository;

    @Override
    public Theme create(final CreateThemeServiceRequest request) {
        return themeRepository.save(
                request.toDomain());
    }

    @Override
    public void delete(final ThemeId id) {
        themeRepository.deleteById(id);
    }
}
