package roomescape.reservation.presentation;

import jakarta.validation.Valid;
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
import roomescape.login.presentation.request.LoginCheckRequest;
import roomescape.reservation.business.service.ReservationService;
import roomescape.reservation.presentation.request.UserReservationRequest;
import roomescape.reservation.presentation.response.AvailableReservationTimeResponse;
import roomescape.reservation.presentation.response.ReservationResponse;

@RestController
@RequestMapping("/reservations")
public class UserReservationController {

    private final ReservationService reservationService;

    public UserReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> findAll() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> add(
            @Valid @RequestBody UserReservationRequest request,
            LoginCheckRequest loginCheckRequest
    ) {
        final ReservationResponse reservationResponse = reservationService.add(request, loginCheckRequest.id());
        return new ResponseEntity<>(reservationResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/themes/{themeId}/times")
    public ResponseEntity<List<AvailableReservationTimeResponse>> findAvailableReservationTime(
            @PathVariable Long themeId,
            @RequestParam String date) {
        return ResponseEntity.ok(reservationService.findAvailableReservationTime(themeId, date));
    }
}
