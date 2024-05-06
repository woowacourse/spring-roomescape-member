package roomescape.theme.service;

import org.springframework.stereotype.Service;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeRequestDto;
import roomescape.theme.dto.ThemeResponseDto;
import roomescape.theme.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(final ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<ThemeResponseDto> findAll() {
        final List<Theme> themes = themeRepository.findAll();
        return themes.stream()
                .map(ThemeResponseDto::new)
                .toList();
    }

    public ThemeResponseDto save(final ThemeRequestDto requestDto) {
        final long id = themeRepository.save(requestDto.toTheme());
        return new ThemeResponseDto(id, requestDto.name(), requestDto.description(), requestDto.thumbnail());
    }

    public void deleteById(final long id) {
        final int deleteCount = themeRepository.deleteById(id);
        if (deleteCount == 0) {
            throw new NoSuchElementException("해당하는 테마가 없습니다.");
        }
    }

    public List<ThemeResponseDto> findPopular() {
        final LocalDate today = LocalDate.now();

        final List<Theme> themes = themeRepository.findPopular(today.minusWeeks(1).toString(), today.minusDays(1).toString());
        return themes.stream()
                .map(ThemeResponseDto::new)
                .toList();
    }
}
