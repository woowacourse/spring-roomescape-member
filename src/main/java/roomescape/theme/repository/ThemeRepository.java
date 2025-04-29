package roomescape.theme.repository;

import java.util.List;
import roomescape.theme.domain.Theme;

public interface ThemeRepository {

    Theme add(Theme theme);

    Theme findByIdOrThrow(Long id);

    void delete(Long id);

    List<Theme> findAll();
}
