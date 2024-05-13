package roomescape.ui.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.ReservationTimeService;
import roomescape.application.ThemeService;
import roomescape.application.dto.response.AvailableTimeResponse;
import roomescape.application.dto.response.ThemeResponse;
import roomescape.ui.controller.dto.ThemeRequest;

@RestController
@RequestMapping("/themes")
@Validated
public class ThemeController {
    private final ThemeService themeService;
    private final ReservationTimeService reservationTimeService;

    public ThemeController(ThemeService themeService, ReservationTimeService reservationTimeService) {
        this.themeService = themeService;
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(@Valid @RequestBody ThemeRequest request) {
        ThemeResponse response = themeService.save(request.toThemeCreationRequest());
        URI location = URI.create("/themes/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public List<ThemeResponse> getThemes() {
        return themeService.findThemes();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTheme(@PathVariable @Positive long id) {
        themeService.delete(id);
    }

    @GetMapping("/popular")
    public List<ThemeResponse> getPopularThemes() {
        return themeService.findPopularThemes();
    }

    @GetMapping("/{id}")
    public List<AvailableTimeResponse> getAvailableTimes(@PathVariable @Positive long id,
                                                         @RequestParam LocalDate date) {
        return reservationTimeService.findAvailableTimes(id, date);
    }
}
