package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.repository.ThemeEntity;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.PopularThemeResult;
import roomescape.service.dto.ThemeCreateCommand;
import roomescape.service.dto.ThemeResult;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
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
        return themeRepository.findPopular().stream()
                .map(p -> new PopularThemeResult(
                        ThemeResult.from(p.getThemeEntity()),
                        p.getReservationCount()))
                .toList();
    }

}
