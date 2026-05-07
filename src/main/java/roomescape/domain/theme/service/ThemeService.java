package roomescape.domain.theme.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.theme.dto.request.ThemeCreatedRequestDto;
import roomescape.domain.theme.dto.response.ThemeResponseDto;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<ThemeResponseDto> getThemes() {
        return convertThemesToDto(themeRepository.findAllThemes());
    }

    public List<ThemeResponseDto> getPopularThemes(LocalDate startDate, LocalDate endDate, Integer limit) {
        return convertThemesToDto(themeRepository.findPopularThemesDateBetween(startDate, endDate, limit));
    }

    private List<ThemeResponseDto> convertThemesToDto(List<Theme> themes) {
        return themes.stream()
            .map(Theme::toResponseDto)
            .toList();
    }

    public ThemeResponseDto saveTheme(ThemeCreatedRequestDto requestDto) {
        Theme theme = Theme.create(requestDto.name(), requestDto.description(), requestDto.imageUrl());
        return themeRepository.save(theme).toResponseDto();
    }

    public void deleteThemeById(Long id) {
        themeRepository.deleteThemeById(id);
    }
}
