package roomescape.domain.theme.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.theme.dto.response.ThemeResponseDto;
import roomescape.domain.theme.service.ThemeService;

@RestController
@RequestMapping("/api/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping()
    public ResponseEntity<List<ThemeResponseDto>> getThemes() {
        return ResponseEntity.ok(themeService.getThemes());
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponseDto>> getPopularThemes(
        @RequestParam LocalDate startDate, @RequestParam LocalDate endDate, @RequestParam Integer limit
    ) {
        return ResponseEntity.ok(themeService.getPopularThemes(startDate, endDate, limit));
    }
}
