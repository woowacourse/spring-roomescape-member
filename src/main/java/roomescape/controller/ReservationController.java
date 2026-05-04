package roomescape.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.service.ReservationService;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ReservationResponse>> getReservation() {
        List<ReservationResponse> reservations = reservationService.readReservation();
        return ResponseEntity.ok().body(reservations);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<ReservationResponse> addReservation(@RequestBody ReservationRequest reservationRequest) {
        ReservationResponse response = reservationService.registerReservation(reservationRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.removeReservation(id);
        return ResponseEntity.noContent().build();
    }

}
