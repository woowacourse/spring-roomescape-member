package roomescape.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.domain.policy.PopularThemeCriteria;
import roomescape.repository.ThemeRepository;
import roomescape.repository.projection.ThemeEntity;
import roomescape.service.dto.PopularThemeResult;
import roomescape.service.dto.ThemeCreateCommand;
import roomescape.service.dto.ThemeResult;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final Clock clock;

    public ThemeService(ThemeRepository themeRepository, Clock clock) {
        this.themeRepository = themeRepository;
        this.clock = clock;
    }

    public List<ThemeResult> findAll() {
        return themeRepository.findAll().stream()
                .map(ThemeResult::from)
                .toList();
    }

    public ThemeResult create(ThemeCreateCommand command) {
        Theme theme = new Theme(command.getName(), command.getDescription(), command.getThumbnail());

        ThemeEntity saved = themeRepository.save(theme);

        return ThemeResult.from(saved);
    }

    public void delete(Long id) {
        themeRepository.deleteById(id);
    }

    public List<PopularThemeResult> findPopular() {

        PopularThemeCriteria criteria = PopularThemeCriteria.recentWeekTop10();
        LocalDate today = LocalDate.now(clock);

        return themeRepository.findPopularBetween(
                        criteria.from(today),
                        criteria.to(today),
                        criteria.getLimit())
                .stream()
                .map(p -> new PopularThemeResult(
                        ThemeResult.from(p.getThemeEntity()),
                        p.getReservationCount()))
                .toList();
    }

}
