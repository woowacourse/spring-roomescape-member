package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.dto.ThemeReqDto;
import roomescape.theme.domain.dto.ThemeResDto;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ThemeService {

    private static final int BETWEEN_DAY_START = 7;
    private static final int BETWEEN_DAY_END = 1;
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResDto add(ThemeReqDto dto) {
        Theme notSavedTheme = new Theme(dto.name(), dto.description(), dto.thumbnail());
        Theme savedTheme = themeRepository.add(notSavedTheme);
        return ThemeResDto.from(savedTheme);
    }

    public List<ThemeResDto> findAll() {
        return themeRepository.findAll().stream()
            .map(ThemeResDto::from)
            .toList();
    }

    public void deleteById(Long id) {
        themeRepository.delete(id);
    }

    public List<ThemeResDto> findTopRankThemes(int size) {
        LocalDate now = LocalDate.now();
        LocalDate from = now.minusDays(BETWEEN_DAY_START);
        LocalDate to = now.minusDays(BETWEEN_DAY_END);
        return themeRepository.findAllOrderByRank(from, to, size).stream()
            .map(ThemeResDto::from)
            .toList();
    }
}
