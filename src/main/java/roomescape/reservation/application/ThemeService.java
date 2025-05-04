package roomescape.reservation.application;

import static roomescape.exception.ThemeErrorCode.THEME_DELETE_CONFLICT;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.reservation.application.dto.ThemeDto;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.repository.ThemeRepository;
import roomescape.exception.BusinessException;
import roomescape.exception.NotFoundException;
import roomescape.reservation.presentation.controller.ThemeRankingCondition;
import roomescape.reservation.presentation.dto.request.ThemeRequest;

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
        Theme themeWithoutId = Theme.createNew(themeRequest.name(), themeRequest.description(),
                themeRequest.thumbnail());
        Long id = themeRepository.save(themeWithoutId);
        Theme theme = Theme.assignId(id, themeWithoutId);
        return ThemeDto.from(theme);
    }

    public void deleteTheme(Long id) {
        try {
            themeRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(THEME_DELETE_CONFLICT);
        }
    }

    public Theme getThemeById(@NotNull Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("찾으려는 테마 id", id));
    }

    public List<ThemeDto> getThemeRanking(ThemeRankingCondition condition) {
        List<Theme> themeRanking = themeRepository.findThemeRanking(
                condition.startDate(), condition.endDate(), condition.limit());
        return ThemeDto.from(themeRanking);
    }
}
