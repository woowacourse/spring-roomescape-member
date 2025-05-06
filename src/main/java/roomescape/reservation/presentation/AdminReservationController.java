package roomescape.reservation.presentation;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.business.service.ReservationService;
import roomescape.reservation.presentation.request.AdminReservationRequest;
import roomescape.reservation.presentation.response.ReservationResponse;

@RestController
@RequestMapping("/admin")
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> add(
            @Valid @RequestBody AdminReservationRequest request
    ) {
        ReservationResponse response = reservationService.addAdminReservation(request);
        return ResponseEntity.created(URI.create("/admin/reservations/" + response.id()))
                .body(response);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getFilteredReservations(
            @RequestParam(name = "themeId") long themeId,
            @RequestParam(name = "memberId") long memberId,
            @RequestParam(name = "dateFrom") LocalDate start,
            @RequestParam(name = "dateTo") LocalDate end
    ) {
        List<ReservationResponse> reservationResponses = reservationService.findReservationByTimeAndDateInDuration(
                themeId, memberId, start, end);
        return ResponseEntity.ok(reservationResponses);
    }
}
