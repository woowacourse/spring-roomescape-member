package roomescape.controller;

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
import roomescape.dto.request.AdminReservationRequest;
import roomescape.dto.request.FilteredReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final ReservationService reservationService;

    public AdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody AdminReservationRequest adminReservationRequest) {
        ReservationResponse reservationResponse = reservationService.save(adminReservationRequest);
        return ResponseEntity.created(URI.create("reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @GetMapping("reservations/filter")
    public ResponseEntity<List<ReservationResponse>> findReservationsBy(
            @RequestParam("theme-id") Long themeId,
            @RequestParam("member-id") Long memberId,
            @RequestParam("date-from") LocalDate dateFrom,
            @RequestParam("date-to") LocalDate dateTo) {
        FilteredReservationRequest request =
                new FilteredReservationRequest(themeId, memberId, dateFrom, dateTo);
        return ResponseEntity.ok(reservationService.findAllMatching(request));
    }
}
