package roomescape.reservation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.infra.ThemeRepository;
import roomescape.reservation.presentation.dto.request.ThemeSaveRequest;
import roomescape.reservation.presentation.dto.response.ThemeSaveResponse;

@Service
@RequiredArgsConstructor
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeSaveResponse save(ThemeSaveRequest body) {
        Theme newTheme = themeRepository.save(body.toDomain());
        return ThemeSaveResponse.from(newTheme);
    }
}
