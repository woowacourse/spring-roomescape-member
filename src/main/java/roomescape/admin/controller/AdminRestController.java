package roomescape.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.admin.controller.dto.AdminReservationRequest;
import roomescape.admin.controller.dto.AdminReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.service.ReservationService;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class AdminRestController {

    private final ReservationService reservationService;

    @PostMapping("/reservations")
    public ResponseEntity<AdminReservationResponse> createReservation(
            @RequestBody final AdminReservationRequest adminReservationRequest
    ) {
        final Long id = reservationService.saveByAdmin(
                adminReservationRequest.date(),
                adminReservationRequest.themeId(),
                adminReservationRequest.timeId(),
                adminReservationRequest.memberId()
        );
        final Reservation found = reservationService.getById(id);

        return ResponseEntity.status(HttpStatus.CREATED).body(AdminReservationResponse.from(found));
    }
}
