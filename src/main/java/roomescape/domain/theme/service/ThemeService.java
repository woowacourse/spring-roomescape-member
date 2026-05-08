package roomescape.domain.theme.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.theme.dto.request.ThemeCreateRequestDTO;
import roomescape.domain.theme.dto.response.ThemeResponseDTO;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.mapper.ThemeMapper;
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

    public List<ThemeResponseDTO> getPopularThemes() {
        return convertThemesToDTO(
            themeRepository.findPopularThemesDateBetween(LocalDate.now().minusDays(6), LocalDate.now(), 10));
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
