package roomescape.theme.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.application.ThemeService;
import roomescape.theme.presentation.dto.ThemesResponse;

@RestController
@RequestMapping("/themes")
@RequiredArgsConstructor
public class ThemeController {

    private final ThemeService service;

    @GetMapping
    public ResponseEntity<ThemesResponse> getThemes() {
        return ResponseEntity.ok(service.getThemes());
    }

    @GetMapping("/weeks/top")
    public ResponseEntity<ThemesResponse> getPopularThemes() {
        return ResponseEntity.ok(service.getWeeksTopThemes());
    }
}
