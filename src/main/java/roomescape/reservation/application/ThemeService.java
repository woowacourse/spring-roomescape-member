package roomescape.reservation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.infra.ThemeRepository;
import roomescape.reservation.presentation.dto.request.ThemeSaveRequest;
import roomescape.reservation.presentation.dto.response.ThemeFindResponse;
import roomescape.reservation.presentation.dto.response.ThemeSaveResponse;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeService {
    private final ThemeRepository themeRepository;
    private final ScheduleService scheduleService;
    private final Clock clock;

    public ThemeSaveResponse save(ThemeSaveRequest body) {
        Theme newTheme = themeRepository.save(body.toDomain());
        return ThemeSaveResponse.from(newTheme);
    }

    public void delete(long id) {
        scheduleService.validateThemeDeletable(id);
        themeRepository.deleteById(id);
    }

    public List<ThemeFindResponse> findScheduledThemesByDate(LocalDate date) {
        List<Theme> themes = themeRepository.findScheduledThemesByDate(date);
        return ThemeFindResponse.from(themes);
    }

    public List<ThemeFindResponse> findPopularTheme() {
        LocalDate today = LocalDate.now(clock);
        List<Theme> themes = themeRepository.findPopularThemeByCurrentDate(today);
        return ThemeFindResponse.from(themes);
    }

    public List<ThemeFindResponse> findAll() {
        List<Theme> themes = themeRepository.findAll();
        return ThemeFindResponse.from(themes);
    }
}
