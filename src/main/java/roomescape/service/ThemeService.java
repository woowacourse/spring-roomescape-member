package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.ThemeCreateRequest;
import roomescape.controller.dto.ThemeFamousFindRequest;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {
    private static final long DEFAULT_DAYS = 7;
    private static final long DEFAULT_LIMIT = 10;
    private static final String THEME_DOES_NOT_EXISTS = "존재하지 않는 테마입니다";

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme find(long themeId) {
        return themeRepository.findById(themeId).orElseThrow(
                () -> new IllegalArgumentException(THEME_DOES_NOT_EXISTS)
        );
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public Theme create(ThemeCreateRequest request) {
        Theme theme = Theme.of(request.getName(), request.getDescription(), request.getThumbnailUrl());
        return themeRepository.save(theme);
    }

    public void delete(long id) {
        themeRepository.deleteById(id);
    }

    public List<Theme> findFamous(ThemeFamousFindRequest request) {
        Long days = request.getDays();
        LocalDate date = request.getDate();
        Long limit = request.getLimit();

        if (days == null) {
            days = DEFAULT_DAYS;
        }
        if (limit == null) {
            limit = DEFAULT_LIMIT;
        }
        if (date == null) {
            date = LocalDate.now();
        }

        return themeRepository.findFamous(days, date, limit);
    }
}
