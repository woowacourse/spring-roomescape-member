package roomescape.reservation.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.reservation.dto.theme.ThemeRequest;
import roomescape.reservation.dto.theme.ThemeResponse;
import roomescape.reservation.dto.theme.WeeklyThemeResponse;
import roomescape.reservation.service.ReservationThemeService;

@Controller
@RequestMapping("/themes")
public class ReservationThemeController {

    private final ReservationThemeService reservationThemeService;

    public ReservationThemeController(ReservationThemeService reservationThemeService) {
        this.reservationThemeService = reservationThemeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<ThemeResponse> themes = reservationThemeService.getAllThemes();
        return ResponseEntity.ok().body(themes);
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(@Valid @RequestBody ThemeRequest themeRequest) {
        ThemeResponse response = reservationThemeService.insertTheme(themeRequest);
        return ResponseEntity.created(URI.create("/themes/" + response.id())).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationThemeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/weeklyBest")
    public ResponseEntity<List<WeeklyThemeResponse>> weeklyTheme() {
        List<WeeklyThemeResponse> reservationThemes = reservationThemeService.getWeeklyBestThemes();
        return ResponseEntity.ok().body(reservationThemes);
    }
}
