package roomescape.user.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.user.dto.ThemeResponse;
import roomescape.user.service.ThemeService;

@RestController
@RequestMapping("/user")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponse>> readAll() {
        List<ThemeResponse> themes = themeService.findAll().stream()
                .map(ThemeResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(themes);
    }

    @GetMapping("/themes/{id}")
    public ResponseEntity<ThemeResponse> readById(@PathVariable Long id) {
        Theme theme = themeService.findById(id);
        ThemeResponse themeResponse = ThemeResponse.from(theme);
        return ResponseEntity.ok(themeResponse);
    }

    @GetMapping(value = "/themes/trending", params = {"from", "to", "limit"})
    public ResponseEntity<List<ThemeResponse>> readByTrend(@RequestParam LocalDate from, @RequestParam LocalDate to,
                                                           @RequestParam int limit) {
        List<ThemeResponse> themes = themeService.findByTrend(from, to, limit).stream()
                .map(ThemeResponse::from)
                .toList();

        return ResponseEntity.ok(themes);
    }
}
