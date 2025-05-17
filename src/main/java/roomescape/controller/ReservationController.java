package roomescape.controller;

import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.config.Authenticated;
import roomescape.dto.ReservationRequest;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<Object> reservationList(
            @RequestParam(value = "memberId", required = false) Long memberId,
            @RequestParam(value = "themeId", required = false) Long themeId,
            @RequestParam(value = "dateFrom", required = false) LocalDate dateFrom,
            @RequestParam(value = "dateTo", required = false) LocalDate dateTo) {
        return ResponseEntity.ok(reservationService.findReservations(memberId, themeId, dateFrom, dateTo));
    }

    @PostMapping
    public ResponseEntity<Object> reservationAdd(@Valid @RequestBody ReservationRequest request,
                                                              @Authenticated Long memberId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.addReservation(request, memberId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> reservationRemove(@PathVariable(name = "id") long id) {
        reservationService.removeReservation(id);
        return ResponseEntity.noContent().build();
    }

}
