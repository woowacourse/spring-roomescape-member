package roomescape.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.service.ReservationService;

@Controller
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/admin")
    public String displayMain() {
        return "/admin/index";
    }

    @GetMapping("/admin/reservation")
    public String displayAdminReservation() {
        return "/admin/reservation";
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponseDto>> readReservations() {
        List<ReservationResponseDto> reservationResponseDtos = reservationService.findAllReservations();
        return ResponseEntity.ok().body(reservationResponseDtos);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponseDto> createReservation(
            @RequestBody ReservationRequestDto reservationRequest
    ) {
        ReservationResponseDto newReservation = reservationService.createReservation(reservationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newReservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<String> deleteReservation(
            @PathVariable("id") long idRequest
    ) {
        reservationService.deleteReservation(idRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
