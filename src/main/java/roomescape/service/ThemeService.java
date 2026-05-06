package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.model.Theme;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final static int RANKS_LIMIT_COUNT = 10;

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse register(ThemeRequest themeRequest) {
        Theme theme = themeRepository.save(themeRequest);
        return ThemeResponse.from(theme);
    }

    public void removeById(Long id) {
        try {
            themeRepository.findById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("삭제하려는 ID가 없습니다.");
        }
        themeRepository.deleteById(id);
    }

    public List<ThemeResponse> readAll() {
        List<Theme> themes = themeRepository.findAll();
        return themes.stream()
                .map(ThemeResponse::from)
                .collect(Collectors.toList());
    }

    public List<ThemeResponse> readRanks() {
        LocalDate currentDay = LocalDate.now().minusDays(1);
        LocalDate lastWeekDay = currentDay.minusWeeks(1);
        List<Theme> themes = themeRepository.findByCurrentDateAndLastWeekDateAndLimit(currentDay.toString(),
                lastWeekDay.toString(),
                RANKS_LIMIT_COUNT);
        return themes.stream()
                .map(ThemeResponse::from)
                .collect(Collectors.toList());
    }

    public ThemeResponse readById(Long id) {
        Theme theme = themeRepository.findById(id);
        return ThemeResponse.from(theme);
    }
}
