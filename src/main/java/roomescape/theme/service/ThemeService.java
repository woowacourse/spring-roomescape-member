package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.theme.ThemeMapper;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.dto.PopularThemeRequestDto;
import roomescape.theme.domain.dto.ThemeReqDto;
import roomescape.theme.domain.dto.ThemeResDto;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository repository;

    public ThemeService(ThemeRepository repository) {
        this.repository = repository;
    }

    public List<ThemeResDto> findAll() {
        return repository.findAll().stream()
                .map(ThemeMapper::toResDto)
                .toList();
    }

    public List<ThemeResDto> findThemesOrderByReservationCount(LocalDate from, LocalDate to,
                                                               PopularThemeRequestDto popularThemeRequestDto) {
        return repository.findThemesOrderByReservationCount(from, to, popularThemeRequestDto).stream()
                .map(ThemeMapper::toResDto)
                .toList();
    }

    public ThemeResDto add(ThemeReqDto dto) {
        Theme notSavedTheme = new Theme(dto.name(), dto.description(), dto.thumbnail());
        Theme savedTheme = repository.add(notSavedTheme);
        return ThemeMapper.toResDto(savedTheme);
    }

    public void deleteById(Long id) {
        repository.delete(id);
    }
}
