package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.ReservationTheme;
import roomescape.dto.ReservationThemeRequestDto;
import roomescape.service.ReservationThemeService;

import java.net.URI;
import java.util.List;

@Controller
public class AdminReservationThemeController {

    private final ReservationThemeService reservationThemeService;

    public AdminReservationThemeController(ReservationThemeService reservationThemeService) {
        this.reservationThemeService = reservationThemeService;
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ReservationTheme>> getThemes() {
        List<ReservationTheme> themes = reservationThemeService.getAllThemes();
        return ResponseEntity.ok().body(themes);
    }

    @PostMapping("/themes")
    public ResponseEntity<ReservationTheme> createTheme(@RequestBody ReservationThemeRequestDto reservationThemeRequestDto) {
        ReservationTheme theme = reservationThemeService.insertTheme(reservationThemeRequestDto);
        return ResponseEntity.created(URI.create("/themes/" + theme.getId())).body(theme);
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationThemeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
