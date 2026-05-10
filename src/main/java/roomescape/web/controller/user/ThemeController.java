package roomescape.web.controller.user;

import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ThemeService;
import roomescape.web.dto.ThemeResponses;
import roomescape.web.dto.ThemeTimeResponses;

@RestController
@RequestMapping("/api/themes")
@Validated
@RequiredArgsConstructor
public class ThemeController {

    private final ThemeService themeService;

    @GetMapping("/{id}")
    public ResponseEntity<ThemeTimeResponses> getThemeReservationStatus(
            @PathVariable
            @Positive(message = "테마 조회 식별자는 양수여야 합니다.") Long id,
            @RequestParam LocalDate date
    ) {
        ThemeTimeResponses response = new ThemeTimeResponses(themeService.getThemeReservationStatus(id, date));

        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<ThemeResponses> getAllThemes() {
        ThemeResponses response = new ThemeResponses(themeService.getAllActiveThemes());

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/popular")
    public ResponseEntity<ThemeResponses> getPopularThemes(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @Positive(message = "조회 수는 양수여야 합니다.")
            @RequestParam int limit
    ) {
        ThemeResponses response = new ThemeResponses(themeService.getPopularThemes(startDate, endDate, limit));

        return ResponseEntity.ok().body(response);
    }
}
