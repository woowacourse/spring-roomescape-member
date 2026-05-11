package roomescape.reservation.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationCreateRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.service.ReservationService;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(
            @Valid @RequestBody ReservationCreateRequest reservationCreateRequest) {
        Reservation reservation = reservationService.create(
                reservationCreateRequest.name(),
                reservationCreateRequest.date(),
                reservationCreateRequest.timeId(),
                reservationCreateRequest.themeId()
        );

        return ResponseEntity.status(CREATED)
                .body(ReservationResponse.from(reservation));
    }

}
