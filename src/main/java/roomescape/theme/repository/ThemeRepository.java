package roomescape.theme.repository;

import roomescape.theme.domain.Theme;

public interface ThemeRepository {

    Theme add(Theme theme);

    Theme findByIdOrThrow(Long id);

    void delete(Long id);
}
