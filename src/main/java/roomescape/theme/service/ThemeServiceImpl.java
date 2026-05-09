package roomescape.theme.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.theme.domain.Theme;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.service.dto.ThemeBestServiceDto;
import roomescape.theme.service.dto.ThemeSaveServiceDto;

@Service
public class ThemeServiceImpl implements ThemeService {

    private final ThemeRepository themeRepository;
    private final Clock clock;
    private final int dayCount;
    private final int rankCount;

    public ThemeServiceImpl(
            ThemeRepository themeRepository,
            Clock clock,
            @Value("${theme.dayCount:7}") int dayCount,
            @Value("${theme.rankCount:10}") int rankCount
    ) {
        this.themeRepository = themeRepository;
        this.clock = clock;
        this.dayCount = dayCount;
        this.rankCount = rankCount;
    }

    @Override
    public List<Theme> getAll() {
        return themeRepository.findAll();
    }

    @Override
    public Theme create(ThemeSaveServiceDto theme) {
        Theme newTheme = new Theme(
                theme.name(),
                theme.description(),
                theme.imageUrl()
        );
        return themeRepository.save(newTheme);
    }

    @Override
    public void deleteById(Long id) {
        if (!themeRepository.deleteById(id)) {
            throw new ThemeNotFoundException(id);
        }
    }

    @Override
    public List<Theme> getBestThemes() {
        ThemeBestServiceDto themeBestServiceDto = new ThemeBestServiceDto(LocalDate.now(clock),
                dayCount, rankCount);
        return themeRepository.findBestThemesByDate(themeBestServiceDto);
    }
}
