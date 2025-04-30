package roomescape.theme.domain;

import java.util.List;

public interface ThemeRepository {

    Long save(Theme theme);

    List<Theme> findAll();

    int deleteById(Long id);

    Theme findById(final Long id);
}
