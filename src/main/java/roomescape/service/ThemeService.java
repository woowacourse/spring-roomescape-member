package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dto.PopularThemeResponse;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.entity.Theme;
import roomescape.exceptions.ThemeDuplicateException;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private static final int START_DATE_INTERVAL = 7;
    private static final int END_DATE_INTERVAL = 1;

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
        if (isAnyMatchName(request.name())) {
            throw new ThemeDuplicateException("중복된 테마명이 존재합니다.", request.name());
        }
        Theme newTheme = repository.save(request.toEntity());
        return ThemeResponse.from(newTheme);
    }

    @Transactional
    public void deleteTheme(long id) {
        repository.deleteById(id);
    }

    public List<PopularThemeResponse> readRecentPopularThemes(int count) {
        LocalDate today = LocalDate.now();
        return repository.findPopularThemesThisWeek(
                        today.minusDays(START_DATE_INTERVAL), today.minusDays(END_DATE_INTERVAL)
                        , count)
                .stream()
                .map(PopularThemeResponse::from)
                .toList();
    }

    private boolean isAnyMatchName(String name) {
        return repository.findAll().stream()
                .anyMatch(current -> current.name().equals(name));
    }
}
