package roomescape.domain.theme.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.theme.dto.request.ThemeCreatedRequestDTO;
import roomescape.domain.theme.dto.response.ThemeResponseDTO;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<ThemeResponseDTO> getThemes() {
        return convertThemesToDTO(themeRepository.findAllThemes());
    }

    public List<ThemeResponseDTO> getPopularThemes(LocalDate startDate, LocalDate endDate, Integer limit) {
        return convertThemesToDTO(themeRepository.findPopularThemesDateBetween(startDate, endDate, limit));
    }

    private List<ThemeResponseDTO> convertThemesToDTO(List<Theme> themes) {
        return themes.stream()
            .map(Theme::toResponseDTO)
            .toList();
    }

    public ThemeResponseDTO saveTheme(ThemeCreatedRequestDTO requestDTO) {
        Theme theme = Theme.create(requestDTO.name(), requestDTO.description(), requestDTO.imageUrl());
        return themeRepository.save(theme).toResponseDTO();
    }

    public void deleteThemeById(Long id) {
        themeRepository.deleteThemeById(id);
    }
}
