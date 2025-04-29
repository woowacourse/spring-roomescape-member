package roomescape.reservation.application.repository;

import roomescape.reservation.presentation.dto.ThemeRequest;

public interface ThemeRepository {
    Long insert(ThemeRequest themeRequest);
}
