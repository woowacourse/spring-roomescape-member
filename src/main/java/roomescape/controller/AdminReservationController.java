package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.request.AdminReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

import java.time.LocalDate;
import java.util.List;

@RestController
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> addByAdmin(@RequestBody AdminReservationRequest adminReservationRequest) {
        return new ResponseEntity<>(reservationService.addByAdmin(adminReservationRequest), HttpStatus.CREATED);
    }

    @GetMapping("/admin/reservations")
    public ResponseEntity<List<ReservationResponse>> searchByAdmin(
        @RequestParam Long memberId,
        @RequestParam Long themeId,
        @RequestParam LocalDate dateFrom,
        @RequestParam LocalDate dateTo) {
        return ResponseEntity.ok(reservationService.search(memberId, themeId, dateFrom, dateTo));
    }
}
