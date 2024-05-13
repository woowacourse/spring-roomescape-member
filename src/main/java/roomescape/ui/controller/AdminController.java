package roomescape.ui.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.ReservationService;
import roomescape.application.dto.request.ReservationFilteringRequest;
import roomescape.application.dto.response.ReservationResponse;
import roomescape.ui.controller.dto.AdminReservationRequest;

@RestController
@RequestMapping("/admin")
@Validated
public class AdminController {
    private final ReservationService reservationService;

    public AdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createMemberReservation(
            @Valid @RequestBody AdminReservationRequest request) {
        ReservationResponse response = reservationService.reserve(request.toReservationCreationRequest());
        URI location = URI.create("/reservations/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> getReservationsForDateRange(@RequestParam @Positive long themeId,
                                                                 @RequestParam @Positive long memberId,
                                                                 @RequestParam LocalDate dateFrom,
                                                                 @RequestParam LocalDate dateTo) {
        ReservationFilteringRequest request = new ReservationFilteringRequest(themeId, memberId, dateFrom, dateTo);
        return reservationService.findReservations(request);
    }
}
