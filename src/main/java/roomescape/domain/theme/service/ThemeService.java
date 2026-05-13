package roomescape.domain.theme.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.BusinessException;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.exception.ThemeErrorCode;
import roomescape.domain.theme.repository.PopularThemeResult;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.theme.repository.ThemeReservationTimeResult;
import roomescape.domain.theme.request.ThemeCreateRequest;
import roomescape.domain.theme.request.ThemeUpdateRequest;
import roomescape.domain.theme.response.PopularThemeResponse;
import roomescape.domain.theme.response.PopularThemesResponse;
import roomescape.domain.theme.response.ThemeReservationTimeResponse;
import roomescape.domain.theme.response.ThemeReservationTimesResponse;
import roomescape.domain.theme.response.ThemeResponse;
import roomescape.domain.theme.response.ThemesResponse;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final Clock clock;

    public ThemeService(ThemeRepository themeRepository, Clock clock) {
        this.themeRepository = themeRepository;
        this.clock = clock;
    }

    public ThemesResponse findAllThemes() {
        List<Theme> themes = themeRepository.findAll();
        List<ThemeResponse> responses = themes.stream()
                .map(ThemeResponse::from)
                .toList();

        return new ThemesResponse(responses);
    }

    public ThemeReservationTimesResponse findAllThemeReservationTimes(Long themeId, LocalDate date) {
        List<ThemeReservationTimeResult> timeResults = themeRepository.findAllReservationTimesByThemeIdAndDate(
                themeId,
                date
        );

        List<ThemeReservationTimeResponse> times = timeResults.stream()
                .map(ThemeReservationTimeResponse::from)
                .toList();

        return new ThemeReservationTimesResponse(times);
    }

    public PopularThemesResponse findPopularThemes(Integer period, Integer limit) {
        LocalDate today = LocalDate.now(clock);
        LocalDate startDate = today.minusDays(period);
        LocalDate endDate = today.minusDays(1);

        List<PopularThemeResult> results = themeRepository.findPopularThemes(
                startDate,
                endDate,
                limit
        );

        List<PopularThemeResponse> popularThemes = results.stream()
                .map(PopularThemeResponse::from)
                .toList();

        return new PopularThemesResponse(popularThemes);
    }

    @Transactional
    public ThemeResponse saveTheme(ThemeCreateRequest request) {
        Theme theme = Theme.create(
                request.name(),
                request.description(),
                request.thumbnailUrl()
        );

        Theme savedTheme = themeRepository.save(theme);

        return ThemeResponse.from(savedTheme);
    }

    @Transactional
    public ThemeResponse updateTheme(Long id, ThemeUpdateRequest request) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ThemeErrorCode.THEME_NOT_FOUND));

        if (!theme.getName().equals(request.name()) && themeRepository.existsByName(request.name())) {
            throw new BusinessException(ThemeErrorCode.THEME_DUPLICATE);
        }

        Theme updatedTheme = Theme.of(id, request.name(), request.description(), request.thumbnailUrl());
        int updatedCount = themeRepository.update(id, updatedTheme);

        if (updatedCount == 0) {
            throw new BusinessException(ThemeErrorCode.THEME_NOT_FOUND);
        }

        return ThemeResponse.from(updatedTheme);
    }

    @Transactional
    public void deleteThemeById(Long themeId) {
        try {
            int deletedCount = themeRepository.deleteById(themeId);
            if (deletedCount == 0) {
                throw new BusinessException(ThemeErrorCode.THEME_NOT_FOUND);
            }
        } catch (DataIntegrityViolationException exception) {
            throw new BusinessException(ThemeErrorCode.THEME_DELETE_CONFLICT, exception);
        }
    }
}
