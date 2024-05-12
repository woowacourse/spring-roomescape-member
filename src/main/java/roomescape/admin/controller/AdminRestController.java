package roomescape.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.admin.dto.AdminReservationSaveRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.service.ReservationService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

    private final ReservationService reservationService;

    public AdminRestController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody AdminReservationSaveRequest request) {
        ReservationResponse response = reservationService.saveReservation(request);

        URI location = URI.create("admin/reservations/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> findReservations(@RequestParam Long memberId,
                                                                      @RequestParam Long themeId,
                                                                      @RequestParam LocalDate from,
                                                                      @RequestParam LocalDate to) {
        List<ReservationResponse> responses = reservationService.findByMemberIdAndThemeIdAndDateBetween(memberId, themeId, from, to);
        return ResponseEntity.ok(responses);
    }
}
