package roomescape.theme.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.schedule.repository.ScheduleRepository;
import roomescape.theme.dto.*;
import roomescape.theme.model.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ScheduleRepository scheduleRepository;

    public ThemeService(ThemeRepository themeRepository, ScheduleRepository scheduleRepository) {
        this.themeRepository = themeRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public ThemesResponse findAll() {
        List<Theme> themes = themeRepository.findAll();
        return ThemesResponse.from(themes);
    }

    @Transactional
    public ThemeResponse create(ThemeRequest request) {
        Theme theme = new Theme(request.name(), request.description(), request.imageUrl(), request.requiredTime());
        Long id = themeRepository.create(theme);
        return ThemeResponse.from(new Theme(id, request.name(), request.description(), request.imageUrl(), request.requiredTime()));
    }

    @Transactional
    public void delete(Long id) {
        if (scheduleRepository.existsByThemeId(id)) {
            throw new IllegalArgumentException("사용 중인 테마는 삭제할 수 없습니다.");
        }
        themeRepository.delete(id);
    }

    public PopularThemesResponse findPopularThemes(String sort, int limit, int days) {
        List<PopularThemeResponse> responses = themeRepository.findPopularThemes(sort, limit, days);
        return PopularThemesResponse.from(responses);
    }
}
