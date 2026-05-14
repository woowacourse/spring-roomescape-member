package roomescape.domain.theme.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.domain.theme.dto.request.ThemeCreateRequestDto;
import roomescape.domain.theme.dto.response.ThemeResponseDto;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.mapper.ThemeMapper;
import roomescape.domain.theme.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final Clock clock;

    @Autowired
    public ThemeService(ThemeRepository themeRepository) {
        this(themeRepository, Clock.systemDefaultZone());
    }

    public ThemeService(ThemeRepository themeRepository, Clock clock) {
        this.themeRepository = themeRepository;
        this.clock = clock;
    }

    public List<ThemeResponseDto> getThemes() {
        return convertThemesToDto(themeRepository.findAllByDeletedAtIsNull());
    }

    public List<ThemeResponseDto> getPopularThemes() {
        LocalDate today = LocalDate.now(clock);
        LocalDate startDate = today.minusDays(7);
        LocalDate endDate = today.minusDays(1);

        return convertThemesToDto(
            themeRepository.findPopularThemesDateBetween(startDate, endDate, 10));
    }

    private List<ThemeResponseDto> convertThemesToDto(List<Theme> themes) {
        return themes.stream()
            .map(ThemeMapper::toResponseDto)
            .toList();
    }

    public ThemeResponseDto saveTheme(ThemeCreateRequestDto requestDto) {
        Theme theme = Theme.create(requestDto.name(), requestDto.description(), requestDto.imageUrl());
        return ThemeMapper.toResponseDto(themeRepository.save(theme));
    }

    public void deleteThemeById(Long id) {
        themeRepository.deleteThemeById(id);
    }
}
