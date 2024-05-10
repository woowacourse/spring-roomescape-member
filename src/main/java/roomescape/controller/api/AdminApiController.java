package roomescape.controller.api;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.api.dto.request.AdminReservationCreateRequest;
import roomescape.controller.api.dto.response.ReservationResponse;
import roomescape.service.ReservationService;
import roomescape.service.dto.output.ReservationOutput;

@RestController
@RequestMapping("/admin")
public class AdminApiController {

    private final ReservationService reservationService;

    public AdminApiController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody final AdminReservationCreateRequest request) {
        final ReservationOutput output = reservationService.createReservation(request.toInput());
        return ResponseEntity.created(URI.create("/reservations/" + output.id()))
                .body(ReservationResponse.from(output));
    }
}
