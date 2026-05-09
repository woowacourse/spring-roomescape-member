package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequestDTO;
import roomescape.dto.ThemeResponseDTO;
import roomescape.repository.JdbcThemeRepository;
import roomescape.repository.ThemeRepository;

@Service
@Transactional
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(JdbcThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponseDTO addTheme(ThemeRequestDTO request) {
        Theme theme =
                Theme.withoutId(request.name(), request.description(), request.imageUrl());
        Theme savedTheme = themeRepository.save(theme);
        return ThemeResponseDTO.from(savedTheme);
    }

    public List<ThemeResponseDTO> findAllThemes() {
        return themeRepository.findAll()
                .stream()
                .map(ThemeResponseDTO::from)
                .toList();
    }

    public ThemeResponseDTO findById(Long id) {
        Theme result = themeRepository.findById(id)
                .orElseThrow();
        return ThemeResponseDTO.from(result);
    }

    public List<ThemeResponseDTO> findPopularThemes() {
        return themeRepository.findPopularThemes()
                .stream()
                .map(ThemeResponseDTO::from)
                .toList();
    }

    public void deleteTheme(Long id) {
        themeRepository.delete(id);
    }
}
