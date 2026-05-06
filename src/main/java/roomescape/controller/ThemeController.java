package roomescape.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.AvailableReservationTimeResponse;
import roomescape.dto.SearchRequest;
import roomescape.dto.ThemeResponse;
import roomescape.service.ThemeService;

@RequestMapping("/themes")
@RestController
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }
    
    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getAllThemes(SearchRequest request) {
        if (request == null) {
            return ResponseEntity.ok(themeService.getAllThemes());
        }

        if (request.condition().equals("popular")) {
            return ResponseEntity.ok(themeService.getPopularThemes(request.size()));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}/times")
    public ResponseEntity<List<AvailableReservationTimeResponse>> getReservationTimes(
            @PathVariable long id,
            @RequestParam String date) {
        final List<AvailableReservationTimeResponse> reservationTimeResponses = themeService.getAvailableTimeResponses(id, date);
        return ResponseEntity.ok(reservationTimeResponses);
    }
}
