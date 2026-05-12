package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequestDTO;
import roomescape.dto.ThemeResponseDTO;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponseDTO addTheme(ThemeRequestDTO request) {
        Theme theme = Theme.create(request.name(), request.description(), request.imageUrl());
        Theme savedTheme = themeRepository.save(theme);
        return ThemeResponseDTO.from(savedTheme);
    }

    public List<ThemeResponseDTO> findAllThemes() {
        return themeRepository.findAll().stream()
                .map(ThemeResponseDTO::from)
                .collect(Collectors.toList());
    }

    public ThemeResponseDTO findById(Long id) {
        Theme result = themeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당하는 id의 테마가 존재하지 않습니다."));
        return ThemeResponseDTO.from(result);
    }

    public List<ThemeResponseDTO> getPopularThemes(Long weeks, Long limit) {
        return themeRepository.findPopularThemes(
                LocalDate.now().minusWeeks(weeks),
                LocalDate.now(),
                limit
        )
                .stream()
                .map(ThemeResponseDTO::from)
                .toList();
    }

    public void deleteTheme(Long id) {
        themeRepository.delete(id);
    }
}
