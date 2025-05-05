package roomescape.theme.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.domain.DomainTerm;
import roomescape.common.exception.DuplicateException;
import roomescape.common.exception.NotFoundException;
import roomescape.theme.application.dto.CreateThemeServiceRequest;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeId;
import roomescape.theme.domain.ThemeName;
import roomescape.theme.domain.ThemeRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class ThemeCommandUseCaseImpl implements ThemeCommandUseCase {

    private final ThemeRepository themeRepository;
    private final ThemeQueryUseCase themeQueryUseCase;

    @Override
    public Theme create(final CreateThemeServiceRequest request) {
        final Theme theme = request.toDomain();
        final ThemeName name = theme.getName();

        if (themeQueryUseCase.existsByName(name)) {
            throw new DuplicateException(DomainTerm.THEME, name);
        }

        return themeRepository.save(theme);
    }

    @Override
    public void delete(final ThemeId id) {
        if (themeQueryUseCase.existsById(id)) {
            themeRepository.deleteById(id);
            return;
        }
        throw new NotFoundException(DomainTerm.THEME, id);
    }
}
