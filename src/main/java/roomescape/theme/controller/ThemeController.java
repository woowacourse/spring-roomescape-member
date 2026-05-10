package roomescape.theme.controller;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.controller.dto.ThemeTimeAvailabilityResponse;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;

@RequestMapping("/themes")
@RestController
@RequiredArgsConstructor
public class ThemeController {
    private final ThemeService themeService;

    @GetMapping
    public List<ThemeResponse> getAllThemes() {
        return themeService.getAllThemes().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @GetMapping(params = "sort")
    public ResponseEntity<List<ThemeResponse>> getThemesByCondition(
            @RequestParam String sort,
            @RequestParam(defaultValue = "10") int limit) {

        return switch (sort) {
            case "popular" -> ResponseEntity.ok(themeService.getPopularThemes(limit).stream()
                    .map(ThemeResponse::from)
                    .toList());
            default -> ResponseEntity.badRequest().build();
        };
    }

    @GetMapping("/{id}/times")
    public List<ThemeTimeAvailabilityResponse> getReservationTimes(
            @PathVariable long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return themeService.getThemeTimeAvailability(id, date).stream()
                .map(ThemeTimeAvailabilityResponse::from)
                .toList();
    }
}
