package roomescape.domain.theme.controller;

import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.theme.response.PopularThemesResponse;
import roomescape.domain.theme.response.ThemeReservationTimesResponse;
import roomescape.domain.theme.response.ThemesResponse;
import roomescape.domain.theme.service.ThemeService;

@Validated
@RestController
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/themes")
    public ResponseEntity<ThemesResponse> getAllThemes() {
        ThemesResponse themes = themeService.findAllThemes();
        return ResponseEntity.ok(themes);
    }

    @GetMapping("/themes/{themeId}/times")
    public ResponseEntity<ThemeReservationTimesResponse> getAllThemeReservationTimes(
            @PathVariable Long themeId,
            @RequestParam LocalDate date
    ) {
        ThemeReservationTimesResponse times = themeService.findAllThemeReservationTimes(themeId, date);
        return ResponseEntity.ok(times);
    }

    @GetMapping("/themes/popular")
    public ResponseEntity<PopularThemesResponse> getPopularThemes(
            @RequestParam @Positive(message = "period는 양수만 가능합니다.") Integer period,
            @RequestParam @Positive(message = "limit은 양수만 가능합니다.") Integer limit
    ) {
        PopularThemesResponse popularThemes = themeService.findPopularThemes(period, limit);
        return ResponseEntity.ok(popularThemes);
    }
}
