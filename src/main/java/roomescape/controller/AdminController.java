package roomescape.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import roomescape.dto.request.ReservationAdminCreateRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final ReservationService service;

    public AdminController(ReservationService service) {
        this.service = service;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationAdminCreateRequest dto) {
        ReservationResponse response = service.createReservation(dto);

        URI location = URI.create("/admin/reservations/" + response.id());
        return ResponseEntity
                .created(location)
                .body(response);
    }
}
