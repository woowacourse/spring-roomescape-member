package roomescape.reservation.ui;

import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.dto.AdminReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.application.ReservationService;

@RestController
public class AdminReservationApiController {

    private final ReservationService reservationService;

    public AdminReservationApiController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/admin/reservations")
    public ResponseEntity<List<ReservationResponse>> findBySearchFilter(
            @RequestParam(required = false) Long themeId,
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo
    ) {
        return ResponseEntity.ok(reservationService.findBySearchFilter(
                themeId, memberId, dateFrom, dateTo));
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> addByAdmin(
            @RequestBody AdminReservationRequest adminReservationRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.addByAdmin(adminReservationRequest));
    }
}
