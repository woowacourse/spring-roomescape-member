package roomescape.domain.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.entity.Theme;
import roomescape.dto.response.PopularThemeResponse;

public interface ThemeRepository {

    Theme save(final Theme theme);

    List<Theme> findAll();

    List<PopularThemeResponse> findAllPopular();

    void deleteById(final Long id);

    Optional<Theme> findById(final Long id);
}
