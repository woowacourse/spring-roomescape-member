package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.ThemeRequest;
import roomescape.controller.dto.ThemeResponse;
import roomescape.domain.Theme;
import roomescape.service.ReservationService;
import roomescape.service.ThemeService;
import roomescape.service.dto.TimeAvailabilityDto;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;
    private final ReservationService reservationService;

    public ThemeController(ThemeService themeService, ReservationService reservationService) {
        this.themeService = themeService;
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<ThemeResponse> getThemes() {
        return themeService.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(@Valid @RequestBody ThemeRequest request) {
        Theme theme = themeService.create(
                request.name(),
                request.description(),
                request.thumbnail());
        return ResponseEntity.created(URI.create("/themes/" + theme.getId()))
                .body(ThemeResponse.from(theme));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/times")
    public List<TimeAvailabilityDto> getAvailableTimes(@PathVariable Long id, @RequestParam("date") LocalDate date) {
        return reservationService.findAvailableTime(id, date);
    }

    @GetMapping("/popular")
    public List<ThemeResponse> getPopularThemes() {
        return themeService.findWeeklyTopTen().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
