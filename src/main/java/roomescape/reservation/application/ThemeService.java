package roomescape.reservation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.infra.ScheduleRepository;
import roomescape.reservation.infra.ThemeRepository;
import roomescape.reservation.presentation.dto.request.ThemeSaveRequest;
import roomescape.reservation.presentation.dto.response.ThemeFindResponse;
import roomescape.reservation.presentation.dto.response.ThemeSaveResponse;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeService {
    private final ThemeRepository themeRepository;
    private final ScheduleRepository scheduleRepository;

    public ThemeSaveResponse save(ThemeSaveRequest body) {
        Theme newTheme = themeRepository.save(body.toDomain());
        return ThemeSaveResponse.from(newTheme);
    }

    public void delete(long id) {
        if (scheduleRepository.existsByThemeId(id)) {
            throw new IllegalStateException("themeId= " + id + " 인 테마를 사용하는 스케줄이 있어 삭제할 수 없습니다.");
        }
        themeRepository.deleteById(id);
    }

    public List<ThemeFindResponse> findScheduledThemesByDate(LocalDate date) {
        List<Theme> themes = themeRepository.findScheduledThemesByDate(date);
        return ThemeFindResponse.of(themes);
    }

    public List<ThemeFindResponse> findByDayAndLimit() {
        List<Theme> themes = themeRepository.findByDayAndLimit();
        return ThemeFindResponse.of(themes);
    }

    public List<ThemeFindResponse> findAll() {
        List<Theme> themes = themeRepository.findAll();
        return ThemeFindResponse.of(themes);
    }
}
