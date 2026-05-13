package roomescape.theme.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.entity.Theme;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.payload.ThemeRequest;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final Clock clock;

    public ThemeService(ThemeRepository themeRepository, Clock clock) {
        this.themeRepository = themeRepository;
        this.clock = clock;
    }

    @Transactional
    public Theme save(ThemeRequest request) {
        Theme theme = Theme.create(
                request.name(),
                request.description(),
                request.thumbnailUrl(),
                Theme.RUNTIME
        );

        return themeRepository.save(theme);
    }

    @Transactional(readOnly = true)
    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Theme> findPopularThemes(int recentDays, int limit) {
        LocalDate today = LocalDate.now(clock);
        LocalDate start = today.minusDays(recentDays);

        return themeRepository.findPopularThemes(start, today, limit);
    }

    @Transactional
    public void deleteById(Long id) {
        int affected = themeRepository.deleteById(id);
        if (affected == 0) {
            throw new ThemeNotFoundException(id);
        }
    }

}
