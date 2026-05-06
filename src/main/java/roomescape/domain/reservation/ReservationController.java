package roomescape.domain.reservation;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.admin.AdminRequestValidator;
import roomescape.domain.reservation.dto.CreateReservationRequest;
import roomescape.domain.reservation.dto.CreateReservationResponse;
import roomescape.domain.reservation.dto.ReservationResponse;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final AdminRequestValidator validator;

    @GetMapping("/admin/reservations")
    public ResponseEntity<List<ReservationResponse>> getAllReservation(HttpServletRequest request) {
        if (validator.isUnauthorized(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<ReservationResponse> response = reservationService.getAllReservations();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reservations")
    public ResponseEntity<CreateReservationResponse> createReservation(@RequestBody CreateReservationRequest request) {
        request.validate();
        CreateReservationResponse response = reservationService.createReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/admin/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(HttpServletRequest request, @PathVariable Long id) {
        if (validator.isUnauthorized(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        reservationService.deleteReservation(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
