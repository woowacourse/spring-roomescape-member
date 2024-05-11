package roomescape.controller.api;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeRequestDto;
import roomescape.dto.response.ThemeResponseDto;
import roomescape.service.ThemeService;

@RestController
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponseDto> createTheme(@RequestBody @Valid final ThemeRequestDto request) {
        final Theme theme = themeService.create(request);
        final ThemeResponseDto response = new ThemeResponseDto(theme);
        return ResponseEntity.created(URI.create("/themes/" + response.getId()))
                .body(response);
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponseDto>> findAllThemes() {
        final List<Theme> themes = themeService.findAll();
        final List<ThemeResponseDto> response = themes.stream()
                .map(ThemeResponseDto::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/themes/popular")
    public ResponseEntity<List<ThemeResponseDto>> findPopularThemesByPeriod(
            @RequestParam("period-day") final Long periodDay
    ) {
        final List<Theme> themes = themeService.findPopularThemesByPeriod(periodDay);
        final List<ThemeResponseDto> response = themes.stream()
                .map(ThemeResponseDto::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable("id") final long id) {
        themeService.delete(id);
        return ResponseEntity.noContent()
                .build();
    }
}
