package roomescape.theme.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.dto.*;
import roomescape.theme.model.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
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

    public void delete(Long id) {
        themeRepository.delete(id);
    }

    public PopularThemesResponse findPopularThemes(String sort, int limit, int days) {
        List<PopularThemeResponse> responses = themeRepository.findPopularThemes(sort, limit, days);
        return PopularThemesResponse.from(responses);
    }

    public Theme findById(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
    }
}
