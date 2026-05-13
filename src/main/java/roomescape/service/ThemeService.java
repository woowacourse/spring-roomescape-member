package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.model.Theme;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private static final int RANKS_LIMIT_COUNT = 10;

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse register(ThemeRequest themeRequest) {
        Theme theme = themeRepository.save(themeRequest.name(), themeRequest.description(), themeRequest.url());
        return ThemeResponse.from(theme);
    }

    public void removeById(Long id) {
        themeRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("[ERROR] 삭제하고자 하는 테마 ID가 없습니다.")
        );
        themeRepository.deleteById(id);
    }

    public List<ThemeResponse> readAll() {
        List<Theme> themes = themeRepository.findAll();
        return themes.stream()
                .map(ThemeResponse::from)
                .collect(Collectors.toList());
    }

    public List<ThemeResponse> readRanks(LocalDate today) {
        LocalDate endDate = today.minusDays(1);
        LocalDate startDate = today.minusDays(7);
        List<Theme> themes = themeRepository.findByCurrentDateAndLastWeekDateAndLimit(endDate,
                startDate,
                RANKS_LIMIT_COUNT);
        return themes.stream()
                .map(ThemeResponse::from)
                .collect(Collectors.toList());
    }
}
