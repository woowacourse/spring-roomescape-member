package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.AvailableReservationResponse;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    public static final String DEFAULT_RANK_COUNT = "10";
    public static final String DEFAULT_START_DAYS_AGO = "7";
    public static final String DEFAULT_END_DAYS_AGO = "1";

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> addTheme(@RequestBody @Valid ThemeRequest request) {
        ThemeResponse themeResponse = themeService.addTheme(request);
        return ResponseEntity.created(URI.create("/themes/" + themeResponse.id())).body(themeResponse);
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        return ResponseEntity.ok(themeService.getThemes());
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponse>> getTopTenTheme(
            @RequestParam(defaultValue = DEFAULT_RANK_COUNT) int count,
            @RequestParam(defaultValue = DEFAULT_START_DAYS_AGO) int startDaysAgo,
            @RequestParam(defaultValue = DEFAULT_END_DAYS_AGO) int endDaysAgo
    ) {
        return ResponseEntity.ok(themeService.getTopTheme(count, startDaysAgo, endDaysAgo));
    }

    @GetMapping("/{id}/times")
    public ResponseEntity<List<AvailableReservationResponse>> getThemesTimesWithStatus(
            @PathVariable(name = "id") Long themeId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        return ResponseEntity.ok(themeService.getThemesTimesWithStatus(themeId, date));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable(value = "id") Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
