package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dto.PopularThemeResponse;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.entity.Theme;
import roomescape.exceptions.ThemeDuplicateException;
import roomescape.repository.ThemeRepository;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository repository;

    public ThemeService(ThemeRepository repository) {
        this.repository = repository;
    }

    public List<ThemeResponse> readAllTheme() {
        return repository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @Transactional
    public ThemeResponse postTheme(ThemeRequest request) {
        if (repository.existsByName(request.name())) {
            throw new ThemeDuplicateException("중복된 테마명이 존재합니다.", request.name());
        }
        Theme newTheme = repository.save(request.toEntity());
        return ThemeResponse.from(newTheme);
    }

    @Transactional
    public void deleteTheme(long id) {
        repository.deleteById(id);
    }

    @Transactional
    public List<PopularThemeResponse> readRecentPopularThemes() {
        return repository.findPopularThemesThisWeek().stream()
                .map(PopularThemeResponse::from)
                .toList();
    }
}
