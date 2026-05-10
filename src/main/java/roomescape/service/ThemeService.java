package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
        Theme theme = new Theme(null, themeRequest.name(), themeRequest.description(), themeRequest.url());
        Theme saved = themeRepository.save(theme);
        return ThemeResponse.from(saved);
    }

    public void removeById(Long id) {
        int deleteCnt = themeRepository.deleteById(id);
        if(deleteCnt == 0) {
            throw new IllegalArgumentException("존재하지 않는 테마의 ID 입니다.");
        }
    }

    public List<ThemeResponse> readAll() {
        List<Theme> themes = themeRepository.findAll();
        return themes.stream()
                .map(ThemeResponse::from)
                .collect(Collectors.toList());
    }

    public List<ThemeResponse> readRanks() {
        LocalDate currentDay = LocalDate.now().minusDays(1);
        LocalDate lastWeekDay = LocalDate.now().minusWeeks(1);
        List<Theme> themes = themeRepository.findByCurrentDateAndLastWeekDateAndLimit(currentDay.toString(),
                lastWeekDay.toString(),
                RANKS_LIMIT_COUNT);
        return themes.stream()
                .map(ThemeResponse::from)
                .collect(Collectors.toList());
    }

    public ThemeResponse readById(Long id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 테마입니다."));
        return ThemeResponse.from(theme);
    }
}
