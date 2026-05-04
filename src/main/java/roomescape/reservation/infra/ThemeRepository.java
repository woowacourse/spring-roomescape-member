package roomescape.reservation.infra;

import roomescape.reservation.domain.Theme;

public interface ThemeRepository {
    Theme save(Theme domain);

    void deleteById(long id);
}
