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
import roomescape.admin.AdminRequestValidator;
import roomescape.domain.reservationdate.dto.AdminReservationDateResponse;
import roomescape.domain.reservationdate.dto.ReservationDateCreationRequest;
import roomescape.domain.reservationdate.dto.CreateReservationDateResponse;
import roomescape.domain.reservationdate.dto.ReservationDateResponse;

@RestController
@RequiredArgsConstructor
public class ReservationDateController {

    private final ReservationDateService reservationDateService;

    @GetMapping("/reservation-dates")
    public ResponseEntity<List<ReservationDateResponse>> getAllReservationDates() {
        List<ReservationDateResponse> responses = reservationDateService.getAllReservationDate();
        return ResponseEntity.ok(responses);
    }
}
