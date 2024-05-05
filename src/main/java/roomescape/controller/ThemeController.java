package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.dto.AvailableReservationTimeResponse;
import roomescape.dto.SaveThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.service.ThemeService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/themes")
    public List<ThemeResponse> getThemes() {
        return themeService.getThemes()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponse> saveTheme(@RequestBody final SaveThemeRequest request) {
        final Theme savedTheme = themeService.saveTheme(request);

        return ResponseEntity.created(URI.create("/themes/" + savedTheme.getId()))
                .body(ThemeResponse.from(savedTheme));
    }

    @DeleteMapping("/themes/{themeId}")
    public ResponseEntity<Void> deleteTheme(@PathVariable final Long themeId) {
        themeService.deleteTheme(themeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/popular-themes")
    public List<ThemeResponse> getPopularThemes() {
        return themeService.getPopularThemes()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @GetMapping("/available-reservation-times")
    public List<AvailableReservationTimeResponse> getAvailableReservationTimes(@RequestParam("date") final LocalDate date, @RequestParam("theme-id") final Long themeId) {
        return themeService.getAvailableReservationTimes(date, themeId)
                .values()
                .entrySet()
                .stream()
                .map(entry -> AvailableReservationTimeResponse.of(entry.getKey(), entry.getValue()))
                .toList();
    }
}
