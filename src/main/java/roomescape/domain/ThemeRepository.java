package roomescape.domain;

import java.util.List;

public interface ThemeRepository {

    List<Theme> findAll();

    Theme save(Theme theme);

    Theme findById(Long id);

    void deleteById(Long id);
}
