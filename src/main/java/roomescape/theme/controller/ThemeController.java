package roomescape.theme.controller;

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
import roomescape.theme.dto.PopularThemeResponse;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.service.ThemeDefaultService;
import roomescape.theme.service.popular.PopularThemeUseCase;

@RestController
@RequestMapping("themes")
public class ThemeController {
    private final ThemeDefaultService themeDefaultService;
    private final PopularThemeUseCase popularThemeUseCase;

    public ThemeController(ThemeDefaultService themeDefaultService, PopularThemeUseCase popularThemeUseCase) {
        this.themeDefaultService = themeDefaultService;
        this.popularThemeUseCase = popularThemeUseCase;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(@Valid @RequestBody ThemeRequest request) {
        ThemeResponse themeResponse = themeDefaultService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(themeResponse);
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        return ResponseEntity.ok(themeDefaultService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeDefaultService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<PopularThemeResponse>> getPopularThemes() {
        return ResponseEntity.ok(popularThemeUseCase.getPopularThemes());
    }
}
