package roomescape.theme.service;

import org.springframework.stereotype.Service;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.service.dto.request.ThemeRequest;
import roomescape.theme.service.dto.response.ThemeResponse;
import roomescape.theme.entity.ThemeEntity;
import roomescape.exception.ConflictException;
import roomescape.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse createTheme(ThemeRequest request) {
        ThemeEntity newTheme = request.toEntity();
        themeRepository.findByName(newTheme.getName())
                .ifPresent((theme) -> {
                    throw new ConflictException("중복되는 테마가 존재합니다.");
                });
        ThemeEntity saved = themeRepository.save(newTheme);
        return ThemeResponse.from(saved);
    }

    public List<ThemeResponse> getAllThemes() {
        return themeRepository.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public void deleteTheme(final Long id) {
        final boolean deleted = themeRepository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("존재하지 않는 id 입니다.");
        }
    }

    public List<ThemeResponse> getPopularThemes(final int limit) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate startDate = yesterday.minusWeeks(1);
        return themeRepository.findPopularThemesByDateRangeAndLimit(startDate, yesterday, limit)
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
