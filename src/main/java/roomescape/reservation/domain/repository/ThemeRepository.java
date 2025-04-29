package roomescape.reservation.domain.repository;

import roomescape.reservation.domain.Theme;

public interface ThemeRepository {

    Long saveAndReturnId(Theme theme);
}
