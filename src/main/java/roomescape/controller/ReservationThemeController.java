package roomescape.controller;

import java.util.List;
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
import roomescape.dto.ReservationThemeResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/themes")
public class ReservationThemeController {

    private final ReservationService reservationService;

    public ReservationThemeController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping()
    public ResponseEntity<List<ReservationThemeResponse>> reservationThemeList() {
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.findReservationThemes());
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<ReservationThemeResponse>> reservationThemeRankingList() {
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.findPopularThemes());
    }

    @PostMapping()
    public ResponseEntity<ReservationThemeResponse> reservationThemeAdd(
            @RequestBody ReservationThemeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.addReservationTheme(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> reservationThemeRemove(@PathVariable(name = "id") long id) {
        reservationService.removeReservationTheme(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
