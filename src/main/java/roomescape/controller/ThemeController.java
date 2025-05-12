package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import roomescape.config.CheckRole;
import roomescape.domain.Role;
import roomescape.domain.Theme;
import roomescape.dto.request.CreateThemeRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ThemeRankingService;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;
    private final ThemeRankingService themeRankingService;

    public ThemeController(ThemeService themeService, ThemeRankingService themeRankingService) {
        this.themeService = themeService;
        this.themeRankingService = themeRankingService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<Theme> themes = themeService.findAll();
        List<ThemeResponse> responses = themes.stream()
                .map(ThemeResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThemeResponse> getTheme(@PathVariable Long id) {
        Theme theme = themeService.getThemeById(id);
        ThemeResponse response = ThemeResponse.from(theme);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponse>> popularThemes() {
        List<Theme> rankingThemes = themeRankingService.getRankingThemes(LocalDate.now());
        List<ThemeResponse> responses = rankingThemes.stream()
                .map(ThemeResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    @CheckRole(value = Role.ADMIN)
    public ResponseEntity<ThemeResponse> addTheme(
            @RequestBody @Valid CreateThemeRequest request
    ) {
        Theme theme = themeService.addTheme(request);
        ThemeResponse response = ThemeResponse.from(theme);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("/{id}")
    @CheckRole(value = Role.ADMIN)
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteThemeById(id);

        return ResponseEntity.noContent().build();
    }
}
