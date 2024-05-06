package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.ThemeCreateRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ThemeService;

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
