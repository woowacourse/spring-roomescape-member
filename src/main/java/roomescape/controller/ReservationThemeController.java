package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ReservationThemeRequest;
import roomescape.service.ReservationThemeService;

@RestController
@RequestMapping("/themes")
public class ReservationThemeController {

    private final ReservationThemeService reservationThemeService;

    public ReservationThemeController(final ReservationThemeService reservationThemeService) {
        this.reservationThemeService = reservationThemeService;
    }

    @GetMapping
    public ResponseEntity<Object> reservationThemeList() {
        return ResponseEntity.ok(reservationThemeService.findReservationThemes());
    }

    @GetMapping("/ranking")
    public ResponseEntity<Object> reservationThemeRankingList() {
        return ResponseEntity.ok(reservationThemeService.findPopularReservations());
    }

    @PostMapping
    public ResponseEntity<Object> reservationThemeAdd(
            @Valid @RequestBody ReservationThemeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationThemeService.addReservationTheme(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> reservationThemeRemove(@PathVariable(name = "id") long id) {
        reservationThemeService.removeReservationTheme(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
