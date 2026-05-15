package roomescape.domain.theme.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.theme.dto.response.ThemeResponseDto;
import roomescape.domain.theme.service.ThemeService;

@RestController
@RequestMapping("/api/themes")
@Validated
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping()
    public ResponseEntity<List<ThemeResponseDto>> getThemes() {
        return ResponseEntity.status(HttpStatus.OK)
            .body(themeService.getThemes());
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponseDto>> getPopularThemes(
        @NotNull @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
        @NotNull @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
        @NotNull @RequestParam @Min(1) Integer limit
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(themeService.getPopularThemes(startDate, endDate, limit));
    }
}
