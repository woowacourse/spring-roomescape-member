package roomescape.controller;

import java.net.URI;
import java.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ThemeCreateRequest;
import roomescape.dto.ThemeResponse;
import roomescape.dto.ThemeResponses;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<ThemeResponses> findAllThemes() {
        ThemeResponses themeResponses = themeService.findAll();
        return ResponseEntity.ok()
                .body(themeResponses);
    }

    @GetMapping("/hot")//시작일, 종료일, 10개,
    public ResponseEntity<ThemeResponses> findHotThemesByDurationAndCount(
            @RequestParam LocalDate start, @RequestParam LocalDate end, @RequestParam Integer limit) {
        ThemeResponses themeResponses = themeService.findHotThemesByDurationAndCount(start, end, limit);
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
