package roomescape.theme.service;

import org.springframework.stereotype.Service;
import roomescape.theme.ThemeMapper;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.dto.ThemeReqDto;
import roomescape.theme.domain.dto.ThemeResDto;
import roomescape.theme.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {

    private static final int RANKING_PERIOD_START_DAYS_AGO = 7;
    private static final int RANKING_PERIOD_END_DAYS_AGO = 1;
    private final ThemeRepository repository;

    public ThemeService(ThemeRepository repository) {
        this.repository = repository;
    }

    public List<ThemeResDto> findAll() {
        return repository.findAll().stream()
            .map(ThemeMapper::toResDto)
            .toList();
    }

    public List<ThemeResDto> findTopRankThemes(int size) {
        LocalDate now = LocalDate.now();
        LocalDate from = now.minusDays(RANKING_PERIOD_START_DAYS_AGO);
        LocalDate to = now.minusDays(RANKING_PERIOD_END_DAYS_AGO);
        return repository.findAllOrderByRank(from, to, size).stream()
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
