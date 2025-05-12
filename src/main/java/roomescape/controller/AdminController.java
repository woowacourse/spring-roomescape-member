package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.AdminReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.service.ReservationServiceV2;

@RestController
public class AdminController {

    private final ReservationServiceV2 reservationServiceV2;

    public AdminController(final ReservationServiceV2 reservationServiceV2) {
        this.reservationServiceV2 = reservationServiceV2;
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> addReservation(@RequestBody @Valid final AdminReservationRequest request) {
        ReservationResponse response = reservationServiceV2.addReservationForAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
