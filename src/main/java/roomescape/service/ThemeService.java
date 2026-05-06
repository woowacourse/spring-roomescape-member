package roomescape.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequestDTO;
import roomescape.dto.ThemeResponseDTO;
import roomescape.repository.JdbcThemeRepository;

@Service
public class ThemeService {

    private final JdbcThemeRepository jdbcThemeRepository;

    public ThemeService(JdbcThemeRepository jdbcThemeRepository) {
        this.jdbcThemeRepository = jdbcThemeRepository;
    }


    public ThemeResponseDTO addTheme(ThemeRequestDTO request) {
        Theme theme = new Theme(request.name(), request.description(), request.imageUrl());
        Theme savedTheme = jdbcThemeRepository.save(theme);
        return ThemeResponseDTO.from(savedTheme);
    }

    public List<ThemeResponseDTO> findAllThemes() {
        return jdbcThemeRepository.findAll().stream().map(ThemeResponseDTO::from)
                .collect(Collectors.toList());
    }

    public ThemeResponseDTO findById(Long id) {
        Theme result = jdbcThemeRepository.findById(id)
                .orElseThrow();
        return ThemeResponseDTO.from(result);
    }

    public void deleteTheme(Long id) {
        jdbcThemeRepository.delete(id);
    }
}
