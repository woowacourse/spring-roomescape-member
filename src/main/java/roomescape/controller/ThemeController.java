package roomescape.controller;

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
import roomescape.controller.dto.CreateThemeRequest;
import roomescape.controller.dto.ThemeRankResponse;
import roomescape.controller.dto.ThemeResponse;
import roomescape.service.ThemeService;

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
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(themeService.getThemeRankingsInRecentDays(days, limit));
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(@RequestBody CreateThemeRequest createThemeRequest) {
        ThemeResponse themeResponse = themeService.addTheme(createThemeRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(themeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.removeRegisteredTheme(id);
        return ResponseEntity.noContent().build();
    }

}
