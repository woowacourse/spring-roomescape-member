package roomescape.controller.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.request.ThemeAddRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ReservationService;
import roomescape.service.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ReservationService reservationService;
    private final ThemeService themeService;

    public ThemeController(ReservationService reservationService, ThemeService themeService) {
        this.reservationService = reservationService;
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> addTheme(
            @RequestBody ThemeAddRequest themeAddRequest) {
        ThemeResponse themeResponse = themeService.addTheme(themeAddRequest);
        return ResponseEntity.created(URI.create("/times/" + themeResponse.id()))
                .body(themeResponse);
    }

    @GetMapping
    public List<ThemeResponse> findThemes(@RequestParam(required = false) Long limit) {
        if (limit != null) {
            return reservationService.findPopularThemes(limit)
                    .stream()
                    .map(ReservationResponse::theme)
                    .toList();
        }
        return themeService.findThemes();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
