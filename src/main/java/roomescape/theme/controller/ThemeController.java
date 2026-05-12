package roomescape.theme.controller;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.controller.dto.CreateThemeRequest;
import roomescape.theme.controller.dto.ThemeRankResponse;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;
import roomescape.time.controller.dto.request.GetAvailableTimesRequest;
import roomescape.time.controller.dto.response.ThemeReservationTimesResponse;

@RestController
@RequestMapping("/themes")
@RequiredArgsConstructor
public class ThemeController {

    private final ThemeService themeService;

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        return ResponseEntity.ok(themeService.findAllThemes());
    }

    @GetMapping("/rank")
    public ResponseEntity<List<ThemeRankResponse>> getRankedThemes(
            @RequestParam int days,
            @RequestParam int limit
    ) {
        return ResponseEntity.ok(themeService.getThemeRankingsInRecentDays(days, limit));
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(@Valid @RequestBody CreateThemeRequest createThemeRequest) {
        ThemeResponse themeResponse = themeService.addTheme(createThemeRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(themeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.removeRegisteredTheme(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/times")
    public ResponseEntity<ThemeReservationTimesResponse> getAvailableTimes(
            @PathVariable Long id,
            @RequestParam LocalDate date,
            @RequestParam(required = false) Boolean available
    ) {
        return ResponseEntity.ok(themeService.findAllAvailableTimes(
                GetAvailableTimesRequest.of(id, date, available)));
    }
}
