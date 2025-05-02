package roomescape.application;

import static roomescape.exception.ThemeErrorCode.THEME_DELETE_CONFLICT;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.application.dto.ThemeDto;
import roomescape.domain.Theme;
import roomescape.domain.repository.ThemeRepository;
import roomescape.exception.BusinessException;
import roomescape.exception.NotFoundException;
import roomescape.presentation.dto.request.ThemeRequest;

@Service
public class ThemeService {

    public static final int RANKING_LIMIT_COUNT = 10;
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<ThemeDto> getAllThemes() {
        List<Theme> themes = themeRepository.findAll();
        return ThemeDto.from(themes);
    }

    public ThemeDto registerTheme(@Valid ThemeRequest themeRequest) {
        Theme themeWithoutId = Theme.withoutId(themeRequest.name(), themeRequest.description(),
                themeRequest.thumbnail());
        Long id = themeRepository.save(themeWithoutId);
        Theme theme = Theme.assignId(id, themeWithoutId);
        return ThemeDto.from(theme);
    }

    public void deleteTheme(Long id) {
        boolean deleted;
        try {
            deleted = themeRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(THEME_DELETE_CONFLICT);
        }
        if (!deleted) {
            throw new NotFoundException("삭제하려는 테마 id", id);
        }
    }

    public Theme getThemeById(@NotNull Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("찾으려는 테마 id", id));
    }

    public List<ThemeDto> getThemeRanking() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusWeeks(1);
        LocalDate endDate = today.minusDays(1);
        List<Theme> themeRanking = themeRepository.findThemeRanking(RANKING_LIMIT_COUNT, startDate, endDate);
        return ThemeDto.from(themeRanking);
    }
}
