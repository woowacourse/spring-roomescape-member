package roomescape.web.controller.user;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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

    @GetMapping("/{id}/times")
    public ResponseEntity<ThemeTimeResponses> getThemeReservationStatus(
            @PathVariable
            @Positive(message = "테마 조회 식별자는 양수여야 합니다.") Long id,
            @RequestParam LocalDate date
    ) {
        ThemeTimeResponses response = new ThemeTimeResponses(themeService.getThemeReservationStatus(id, date));

        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<ThemeResponses> getAllThemesByPaging(
            @RequestParam
            @PositiveOrZero(message = "페이지 번호는 0 이상이어야 합니다.") int page,
            @RequestParam
            @Positive(message = "조회 개수는 양수여야 합니다.") int size
    ) {
        ThemeResponses response = new ThemeResponses(themeService.getAllActiveThemesByPaging(page, size));

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/popular")
    public ResponseEntity<ThemeResponses> getPopularThemesByPaging(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @Positive(message = "조회 개수는 양수여야 합니다.")
            @RequestParam int limit
    ) {
        ThemeResponses response = new ThemeResponses(themeService.getPopularThemes(startDate, endDate, limit));

        return ResponseEntity.ok().body(response);
    }
}
