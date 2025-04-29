package roomescape.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.ReservationThemeRequest;
import roomescape.dto.ReservationThemeResponse;
import roomescape.service.ReservationThemeService;

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
}
