package roomescape.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.service.ReservationService;
import roomescape.presentation.dto.request.AdminReservationRequest;
import roomescape.presentation.dto.response.ReservationResponse;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ReservationService reservationService;

    public AdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(@Validated @RequestBody AdminReservationRequest request) {
        ReservationResponse response = reservationService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
