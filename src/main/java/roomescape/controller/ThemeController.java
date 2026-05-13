package roomescape.controller;

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
import roomescape.dto.request.ThemeCreateRequest;
import roomescape.dto.request.ThemeDeleteRequest;
import roomescape.dto.response.PopularThemeResponse;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/api/v1/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<ThemeResponse> themeResponses = themeService.getThemes();
        return ResponseEntity.ok().body(themeResponses);
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(@RequestBody ThemeCreateRequest request) {
        ThemeResponse themeResponse = themeService.createTheme(request);
        return ResponseEntity.created(URI.create("/api/v1/themes/" + themeResponse.id())).body(themeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id,
                                            @RequestBody ThemeDeleteRequest request) {
        themeService.deleteTheme(id, request.userName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping(params = {"from", "to"})
    public ResponseEntity<List<PopularThemeResponse>> getPopularTheme(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to) {
        List<PopularThemeResponse> popularThemes = themeService.getPopularThemes(from, to);
        return ResponseEntity.ok().body(popularThemes);
    }
}
