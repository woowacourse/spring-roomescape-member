package roomescape.theme.service;

import org.springframework.stereotype.Service;
import roomescape.global.exception.badRequest.BadRequestException;
import roomescape.global.exception.conflict.ThemeNameConflictException;
import roomescape.global.exception.notFound.ThemeNotFoundException;
import roomescape.theme.entity.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.service.dto.request.ThemeRequest;
import roomescape.theme.service.dto.response.ThemeResponse;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse createTheme(ThemeRequest request) {
        Theme newTheme = request.toEntity();
        themeRepository.findByName(newTheme.getName())
                .ifPresent((theme) -> {
                    throw new ThemeNameConflictException();
                });
        Theme saved = themeRepository.save(newTheme);
        return ThemeResponse.of(saved);
    }

    public List<ThemeResponse> getAllThemes() {
        return themeRepository.findAll()
                .stream()
                .map(ThemeResponse::of)
                .toList();
    }

    public void deleteTheme(final Long id) {
        final boolean deleted = themeRepository.deleteById(id);
        if (!deleted) {
            throw new ThemeNotFoundException(id);
        }
    }

    public List<ThemeResponse> getPopularThemes(final int limit) {
        if (limit <= 0) {
            throw new BadRequestException("인기 테마 조회 개수는 최소 1개 이상이어야 합니다.");
        }
        if (limit > 100) {
            throw new BadRequestException("인기 테마 조회 개수는 최대 100개까지 가능합니다.");
        }
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate startDate = yesterday.minusWeeks(1);
        return themeRepository.findPopularThemesByDateRangeAndLimit(startDate, yesterday, limit)
                .stream()
                .map(ThemeResponse::of)
                .toList();
    }
}
