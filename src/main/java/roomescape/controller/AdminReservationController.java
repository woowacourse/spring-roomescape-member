package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.request.AdminReservationCreateRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/admin")
public class AdminReservationController {
    private final ReservationService reservationService;

    @Autowired
    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> create(@RequestBody @Valid AdminReservationCreateRequest adminReservationCreateRequest) {
        ReservationResponse response = reservationService.createReservation(adminReservationCreateRequest.toReservationCreateRequest(), adminReservationCreateRequest.memberId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
