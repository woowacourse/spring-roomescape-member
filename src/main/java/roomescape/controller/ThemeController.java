package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeWithCount;
import roomescape.dto.theme.AddThemeRequest;
import roomescape.dto.theme.PopularConditionRequest;
import roomescape.dto.theme.PopularThemeResponse;
import roomescape.dto.theme.ThemeResponse;
import roomescape.service.ThemeService;

import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping()
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<Theme> themes = themeService.getAllTheme();
        List<ThemeResponse> themeResponses = themes.stream()
                .map(ThemeResponse::from)
                .toList();

        return ResponseEntity.ok(themeResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThemeResponse> getTheme(@PathVariable long id) {
        Theme theme = themeService.getTheme(id);

        return ResponseEntity.ok(ThemeResponse.from(theme));
    }

    @PostMapping()
    public ResponseEntity<ThemeResponse> addTheme(@RequestBody @Valid AddThemeRequest addThemeRequest) {
        Theme addedTheme = themeService.addTheme(addThemeRequest);

        return new ResponseEntity<>(ThemeResponse.from(addedTheme), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable long id) {
        themeService.deleteTheme(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/popular", params = {"startDate", "endDate", "size"})
    public ResponseEntity<List<PopularThemeResponse>> getPopularTheme(@ModelAttribute @Valid PopularConditionRequest popularConditionRequest) {
        List<ThemeWithCount> themeWithCounts = themeService.getPopularTheme(popularConditionRequest);
        List<PopularThemeResponse> popularThemeResponses = themeWithCounts.stream()
                .map(PopularThemeResponse::from)
                .toList();

        return ResponseEntity.ok(popularThemeResponses);
    }
}
