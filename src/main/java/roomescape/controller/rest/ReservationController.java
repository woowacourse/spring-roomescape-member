package roomescape.controller.rest;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.rest.auth.AuthInfo;
import roomescape.domain.reservation.ReservationQuery;
import roomescape.dto.auth.LoginInfo;
import roomescape.dto.reservation.request.UserReservationRequest;
import roomescape.dto.reservation.response.ReservationAvailableTimeResponse;
import roomescape.dto.reservation.response.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> readReservations(
            @ModelAttribute @Valid ReservationQuery reservationRequest
    ) {
        return ResponseEntity.ok(reservationService.findReservationsByCondition(reservationRequest));
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
            @RequestBody @Valid UserReservationRequest reservationRequest,
            @AuthInfo LoginInfo loginInfo
    ) {
        ReservationResponse reservationResponse = reservationService.createReservation(reservationRequest,
                loginInfo.id());

        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);

        return ResponseEntity.noContent().build();
    }
}
