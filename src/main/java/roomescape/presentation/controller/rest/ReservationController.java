package roomescape.presentation.controller.rest;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.domain.LoginUser;
import roomescape.business.service.ReservationService;
import roomescape.presentation.AuthenticatedUser;
import roomescape.presentation.dto.reservation.ReservationAvailableTimeResponse;
import roomescape.presentation.dto.reservation.ReservationRequest;
import roomescape.presentation.dto.reservation.ReservationResponse;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(
            @AuthenticatedUser final LoginUser loginUser,
            @RequestBody final ReservationRequest reservationRequest
    ) {
        final ReservationResponse reservationResponse = reservationService.create(loginUser.id(), reservationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(reservationResponse);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> readAll() {
        final List<ReservationResponse> reservationResponses = reservationService.findAll();

        return ResponseEntity.ok(reservationResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final Long id) {
        reservationService.remove(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<ReservationAvailableTimeResponse>> readAvailableTimes(
            @RequestParam("date") final LocalDate date,
            @RequestParam("themeId") final Long themeId
    ) {
        final List<ReservationAvailableTimeResponse> availableTimeResponses =
                reservationService.findAvailableTimes(date, themeId);

        return ResponseEntity.ok(availableTimeResponses);
    }
}
