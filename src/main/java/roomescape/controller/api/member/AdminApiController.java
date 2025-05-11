package roomescape.controller.api.member;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.controller.api.reservation.dto.AddAdminReservationRequest;
import roomescape.controller.api.reservation.dto.ReservationResponse;
import roomescape.service.ReservationService;

@Controller
@RequestMapping("/admin")
public class AdminApiController {

    private final ReservationService reservationService;

    public AdminApiController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> addReservation(
            @RequestBody @Valid final AddAdminReservationRequest request) {
        final ReservationResponse response = reservationService.add(request);
        return ResponseEntity.created(URI.create("/reservations/" + response.id())).body(response);
    }
}
