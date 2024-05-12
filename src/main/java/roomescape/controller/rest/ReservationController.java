package roomescape.controller.rest;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.rest.auth.AuthInfo;
import roomescape.dto.auth.LoginMember;
import roomescape.dto.reservation.ReservationAvailableTimeResponse;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.service.ReservationService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> readReservations() {
        return ResponseEntity.ok(reservationService.findAllReservations());
    }

    @GetMapping("/themes/{themeId}")
    public ResponseEntity<List<ReservationAvailableTimeResponse>> readAvailableTimeReservations(
            @PathVariable Long themeId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        return ResponseEntity.ok(reservationService.findReservationByDateAndThemeId(date, themeId));
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody @Valid ReservationRequest reservationRequest,
            @AuthInfo LoginMember loginMember
    ) {
        ReservationResponse reservationResponse = reservationService.createReservation(reservationRequest, loginMember.id());

        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);

        return ResponseEntity.noContent().build();
    }
}
