package roomescape.theme.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.theme.dto.ReservationThemeRequest;
import roomescape.theme.dto.ReservationThemeResponse;
import roomescape.theme.service.ReservationThemeService;

import java.util.List;

@RestController
@RequestMapping("/themes")
public class ReservationThemeController {
    private final ReservationThemeService themeService;

    public ReservationThemeController(ReservationThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationThemeResponse>> getAllThemes() {
        List<ReservationThemeResponse> themes = themeService.getAllThemes();
        return ResponseEntity.ok().body(themes);
    }

    @PostMapping
    public ResponseEntity<ReservationThemeResponse> createTheme(@RequestBody ReservationThemeRequest request) {
        ReservationThemeResponse response = themeService.createTheme(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable("id") Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ReservationThemeResponse>> getPopularThemes(@RequestParam("limit") int limit) {
        List<ReservationThemeResponse> response = themeService.getPopularThemes(limit);
        return ResponseEntity.ok().body(response);
    }
}
