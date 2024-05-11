package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.ReservationService;
import roomescape.application.dto.response.ReservationResponse;
import roomescape.controller.dto.AdminReservationRequest;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final ReservationService reservationService;

    public AdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createMemberReservation(
            @Valid @RequestBody AdminReservationRequest request) {
        ReservationResponse response = reservationService.reserve(request.toReservationCreationRequest());
        URI location = URI.create("/reservations/" + response.id());
        return ResponseEntity.created(location).body(response);
    }
}
