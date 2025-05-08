package roomescape.controller.rest.admin;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.member.Member;
import roomescape.service.ReservationService;
import roomescape.service.request.AdminCreateReservationRequest;
import roomescape.service.response.ReservationResponse;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody @Valid AdminCreateReservationRequest request,
            Member member
    ) {
        ReservationResponse response = reservationService.createReservationByAdmin(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
