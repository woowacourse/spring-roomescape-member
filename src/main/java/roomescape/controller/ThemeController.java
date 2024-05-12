package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.request.ThemeCreateRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> findAllThemes() {
        List<ThemeResponse> themeResponses = themeService.findAll();

        return ResponseEntity.ok()
                .body(themeResponses);
    }

    @GetMapping("/hot/weekly")
    public ResponseEntity<List<ThemeResponse>> findWeeklyHotThemes() {
        List<ThemeResponse> themeResponses = themeService.findWeeklyHotThemes();

        return ResponseEntity.ok()
                .body(themeResponses);
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(
            @RequestBody ThemeCreateRequest themeCreateRequest) {
        ThemeResponse themeResponse = themeService.create(themeCreateRequest);
        return ResponseEntity.created(URI.create("/themes/" + themeResponse.id()))
                .body(themeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.delete(id);
        return ResponseEntity.noContent()
                .build();
    }
}
