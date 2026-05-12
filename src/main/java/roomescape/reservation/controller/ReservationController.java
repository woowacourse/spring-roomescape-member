package roomescape.reservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.controller.dto.ReservationCreateRequest;
import roomescape.reservation.controller.dto.ReservationEditRequest;
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
    public ResponseEntity<ReservationResponse> create(@RequestBody @Valid ReservationCreateRequest request) {
        Reservation reservation = reservationService.create(
                request.guestName(),
                request.date(),
                request.timeId(),
                request.themeId()
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

    @PatchMapping("/{id}")
    public ResponseEntity<ReservationResponse> editDateTime(
            @PathVariable("id") Long id,
            @RequestBody @Valid ReservationEditRequest request,
            @RequestHeader("Authorization") String guestName
    ) {
        return ResponseEntity.ok(
                ReservationResponse.from(
                        reservationService.editDateTime(id, request.date(), request.timeId(), guestName)));
    }

}
