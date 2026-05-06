package roomescape.reservation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.dto.request.ReservationSaveDto;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/admin")
public class ReservationAdminController {

    private final ReservationService reservationService;

    public ReservationAdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<ReservationResponse> responseData = reservationService.readAll().stream()
                .map(ReservationResponse::from)
                .toList();
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationSaveDto dto) {
        Reservation reservation = reservationService.create(dto);
        ReservationResponse responseData = ReservationResponse.from(reservation);
        return ResponseEntity.ok(responseData);
    }

    @PatchMapping("/reservations/{id}")
    public ResponseEntity<ReservationResponse> cancelReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.cancel(id);
        ReservationResponse responseData = ReservationResponse.from(reservation);
        return ResponseEntity.ok(responseData);
    }

}
