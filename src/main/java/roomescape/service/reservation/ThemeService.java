package roomescape.service.reservation;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.reservation.Theme;
import roomescape.dto.reservation.ThemeRequest;
import roomescape.dto.reservation.ThemeResponse;
import roomescape.exceptions.reservation.ThemeDuplicateException;
import roomescape.repository.reservation.ThemeRepository;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository repository;

    public ThemeService(ThemeRepository repository) {
        this.repository = repository;
    }

    public List<ThemeResponse> getThemes() {
        return repository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @Transactional
    public ThemeResponse addTheme(ThemeRequest request) {
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

    public List<ThemeResponse> readPopularThemesByPeriod(int period, int maxResults) {
        return repository.findPopularThemes(period, maxResults).stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
