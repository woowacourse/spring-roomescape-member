package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.ReservationTheme;
import roomescape.dto.theme.ThemeRequest;
import roomescape.service.ReservationThemeService;

import java.net.URI;
import java.util.List;

@Controller
public class ReservationThemeController {

    private final ReservationThemeService reservationThemeService;

    public ReservationThemeController(ReservationThemeService reservationThemeService) {
        this.reservationThemeService = reservationThemeService;
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ReservationTheme>> getThemes() {
        List<ReservationTheme> themes = reservationThemeService.getAllThemes();
        return ResponseEntity.ok().body(themes);
    }

    @PostMapping("/themes")
    public ResponseEntity<ReservationTheme> createTheme(@RequestBody ThemeRequest themeRequest) {
        ReservationTheme theme = reservationThemeService.insertTheme(themeRequest);
        return ResponseEntity.created(URI.create("/themes/" + theme.getId())).body(theme);
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationThemeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/weeklyThemes")
    public ResponseEntity<List<ReservationTheme>> weeklyTheme() {
        List<ReservationTheme> reservationThemes = reservationThemeService.getWeeklyBestThemes();
        return ResponseEntity.ok().body(reservationThemes);
    }

    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException() {
        return ResponseEntity.badRequest().body("예약이 존재하는 테마는 삭제할 수 없습니다.");
    }
}
