package roomescape.controller.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.request.AdminReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminApiController {
    private final ReservationService reservationService;

    public AdminApiController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> saveAdminReservation(@RequestBody @Valid AdminReservationRequest reservationRequest) {
        ReservationResponse reservationResponse = reservationService.save(
                reservationRequest.date(),
                reservationRequest.timeId(),
                reservationRequest.themeId(),
                reservationRequest.memberId()
        );
        return ResponseEntity.created(URI.create("/admin/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getFilteredReservations(
            @RequestParam(name = "themeId") long themeId,
            @RequestParam(name = "memberId") long memberId,
            @RequestParam(name = "dateFrom") LocalDate start,
            @RequestParam(name = "dateTo") LocalDate end
    ) {
        List<ReservationResponse> reservationResponses = reservationService.findReservationBy(themeId, memberId, start, end);
        return ResponseEntity.ok(reservationResponses);
    }
}
