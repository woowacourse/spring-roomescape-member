package roomescape.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.theme.PopularThemeResponse;
import roomescape.dto.theme.ThemeRequest;
import roomescape.dto.theme.ThemeResponse;
import roomescape.service.theme.ThemeService;
import roomescape.service.theme.popular.PopularThemeUseCase;

@RestController
@RequestMapping("themes")
public class ThemeController {
    private final ThemeService themeService;
    private final PopularThemeUseCase popularThemeUseCase;

    public ThemeController(ThemeService themeService, PopularThemeUseCase popularThemeUseCase) {
        this.themeService = themeService;
        this.popularThemeUseCase = popularThemeUseCase;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(@Valid @RequestBody ThemeRequest request) {
        ThemeResponse themeResponse = themeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(themeResponse);
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        return ResponseEntity.ok(themeService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<PopularThemeResponse>> getPopularThemes() {
        return ResponseEntity.ok(popularThemeUseCase.getPopularThemes());
    }
}
