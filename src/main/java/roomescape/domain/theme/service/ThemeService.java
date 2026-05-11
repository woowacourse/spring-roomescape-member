package roomescape.domain.theme.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.domain.theme.dto.request.ThemeCreateRequestDTO;
import roomescape.domain.theme.dto.response.ThemeResponseDTO;
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

    public List<ThemeResponseDTO> getThemes() {
        return convertThemesToDTO(themeRepository.findAllThemes());
    }

    public List<ThemeResponseDTO> getPopularThemes() {
        LocalDate today = LocalDate.now(clock);
        LocalDate startDate = today.minusDays(7);
        LocalDate endDate = today.minusDays(1);

        return convertThemesToDTO(
            themeRepository.findPopularThemesDateBetween(startDate, endDate, 10));
    }

    private List<ThemeResponseDTO> convertThemesToDTO(List<Theme> themes) {
        return themes.stream()
            .map(ThemeMapper::toResponseDTO)
            .toList();
    }

    public ThemeResponseDTO saveTheme(ThemeCreateRequestDTO requestDTO) {
        Theme theme = Theme.create(requestDTO.name(), requestDTO.description(), requestDTO.imageUrl());
        return ThemeMapper.toResponseDTO(themeRepository.save(theme));
    }

    public void deleteThemeById(Long id) {
        themeRepository.deleteThemeById(id);
    }
}
