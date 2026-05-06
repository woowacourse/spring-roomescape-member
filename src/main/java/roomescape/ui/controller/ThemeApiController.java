package roomescape.ui.controller;

import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ThemeService;
import roomescape.ui.controller.dto.ThemeResponse;
import roomescape.ui.controller.dto.ThemeTimesResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/themes")
@Validated
public class ThemeApiController {

    private final ThemeService themeService;

    @GetMapping("/{id}")
    public ResponseEntity<List<ThemeTimesResponse>> getThemeReservationStatus(
            @PathVariable
            @Positive(message = "테마 조회 식별자는 양수여야 합니다.") Long id,
            @RequestParam LocalDate date
    ) {
        List<ThemeTimesResponse> response = themeService.getThemeReservationStatus(id, date)
                .stream()
                .map(ThemeTimesResponse::from)
                .toList();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getAllThemes() {
        List<ThemeResponse> response = themeService.getAllActiveThemes()
                .stream()
                .map(ThemeResponse::from)
                .toList();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponse>> getPopularThemes(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        List<ThemeResponse> response = themeService.getPopularThemes(startDate, endDate)
                .stream()
                .map(ThemeResponse::from)
                .toList();
        return ResponseEntity.ok().body(response);
    }
}
