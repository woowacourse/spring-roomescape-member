package roomescape.reservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.controller.dto.ReservationCreateRequest;
import roomescape.reservation.controller.dto.ReservationListResponse;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.service.ReservationService;


import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@RequestBody @Valid ReservationCreateRequest reservationCreateRequest) {
        Reservation reservation = reservationService.create(
                reservationCreateRequest.guestName(),
                reservationCreateRequest.date(),
                reservationCreateRequest.timeId(),
                reservationCreateRequest.themeId()
        );

        return ResponseEntity.status(CREATED)
                .body(ReservationResponse.from(reservation));
    }

    @GetMapping
    public ResponseEntity<ReservationListResponse> getListByGuestName(@RequestParam("guestName") String guestName) {

        List<Reservation> reservations = reservationService.findByGuestName(guestName);

        return ResponseEntity.ok(
                ReservationListResponse.from(reservations.stream()
                        .map(ReservationResponse::from)
                        .toList()));
    }

}
