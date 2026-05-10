package roomescape.domain.reservationdate;

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
import roomescape.support.auth.AdminRequestValidator;
import roomescape.domain.reservationdate.dto.AdminReservationDateResponse;
import roomescape.domain.reservationdate.dto.CreateReservationDateRequest;
import roomescape.domain.reservationdate.dto.CreateReservationDateResponse;
import roomescape.domain.reservationdate.dto.ReservationDateResponse;

@RestController
@RequiredArgsConstructor
public class ReservationDateController {

    private final ReservationDateService reservationDateService;
    private final AdminRequestValidator validator;

    @GetMapping("/admin/reservation-dates")
    public ResponseEntity<List<AdminReservationDateResponse>> getAllReservationDateForAdmin(
        HttpServletRequest request
    ) {
        if (validator.isUnauthorized(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<AdminReservationDateResponse> response = reservationDateService.getAllReservationDateForAdmin();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/reservation-dates")
    public ResponseEntity<CreateReservationDateResponse> createReservationDate(
        HttpServletRequest httpServletRequest,
        @RequestBody CreateReservationDateRequest createReservationDateRequest
    ) {
        if (validator.isUnauthorized(httpServletRequest)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CreateReservationDateResponse response = reservationDateService
            .createReservationDate(createReservationDateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/admin/reservation-dates/{id}")
    public ResponseEntity<Void> deleteReservationDate(@PathVariable Long id, HttpServletRequest request) {
        if (validator.isUnauthorized(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        reservationDateService.deleteReservationDate(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/reservation-dates")
    public ResponseEntity<List<ReservationDateResponse>> getAllReservationDates() {
        List<ReservationDateResponse> responses = reservationDateService.getAllReservationDate();
        return ResponseEntity.ok(responses);
    }
}
