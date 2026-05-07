package roomescape.theme.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.controller.dto.ThemeRequest;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private static final int DEFAULT_POPULAR_PERIOD = 7;
    private static final int DEFAULT_POPULAR_LIMIT = 10;

    private final ThemeRepository themeRepository;
    private final Clock clock;

    public ThemeService(ThemeRepository themeRepository, Clock clock) {
        this.themeRepository = themeRepository;
        this.clock = clock;
    }

    @Transactional
    public Theme save(ThemeRequest request) {
        if (themeRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("이미 존재하는 테마 이름입니다.");
        }

        Theme theme = Theme.create(request.name(), request.description(), request.thumbnailUrl());
        return themeRepository.save(theme);
    }

    @Transactional
    public void deleteById(Long id) {
        themeRepository.deleteById(id);
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public List<Theme> findPopularThemes() {
        LocalDate endDate = LocalDate.now(clock);
        LocalDate startDate = endDate.minusDays(DEFAULT_POPULAR_PERIOD);

        return themeRepository.findPopularThemes(
                startDate,
                endDate,
                DEFAULT_POPULAR_LIMIT
        );
    }

    public Theme getById(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 테마가 존재하지 않습니다."));
    }
}
