package roomescape.admin.presentation.controller;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.admin.presentation.dto.AdminReservationRequest;
import roomescape.reservation.application.service.ReservationService;
import roomescape.reservation.presentation.dto.ReservationResponse;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ReservationService reservationService;

    public AdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(
            final @RequestBody @Valid AdminReservationRequest request
    ) {
        ReservationResponse reservation = reservationService.createReservation(request);
        return ResponseEntity.status(201).body(reservation);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getFilteredReservations(
            final @RequestParam(required = false) Long themeId,
            final @RequestParam(required = false) Long memberId,
            final @RequestParam(required = false) LocalDate dateFrom,
            final @RequestParam(required = false) LocalDate dateTo
    ) {
        List<ReservationResponse> reservations = reservationService.getFilteredReservations(
                themeId, memberId, dateFrom, dateTo);
        return ResponseEntity.ok().body(reservations);
    }

}
