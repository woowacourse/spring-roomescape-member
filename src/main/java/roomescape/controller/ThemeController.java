package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ThemeListResponse;
import roomescape.dto.ThemeResponse;
import roomescape.service.ThemeService;

import java.time.LocalDate;

@RestController
@RequestMapping("/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<ThemeListResponse> list() {
        return ResponseEntity.ok(
                ThemeListResponse.from(themeService.list()
                        .stream()
                        .map(ThemeResponse::from)
                        .toList())
        );
    }

    @GetMapping("/popularity")
    public ResponseEntity<ThemeListResponse> popularThemes(@RequestParam("days") int days,
                                                           @RequestParam("size") int size) {
        return ResponseEntity.ok(
                ThemeListResponse.from(themeService.findPopularThemes(days, LocalDate.now() ,size)
                        .stream()
                        .map(ThemeResponse::from)
                        .toList())
        );
    }
}
