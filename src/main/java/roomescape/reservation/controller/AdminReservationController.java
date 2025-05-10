package roomescape.reservation.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.dto.request.CreateReservationRequest;
import roomescape.reservation.service.dto.response.ReservationResponse;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {
    private final ReservationService service;

    public AdminReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody @Valid CreateReservationRequest request) {
        ReservationResponse responseDto = service.createReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
