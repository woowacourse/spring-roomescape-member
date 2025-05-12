package roomescape.reservation.controller;

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
import roomescape.reservation.dto.AdminReservationRequest;
import roomescape.reservation.service.AdminReservationService;
import roomescape.member.domain.Visitor;
import roomescape.reservation.dto.ReservationCreateResponse;
import roomescape.reservation.dto.ReservationResponse;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    private final AdminReservationService adminReservationService;

    public AdminReservationController(AdminReservationService adminReservationService) {
        this.adminReservationService = adminReservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationCreateResponse> create(Visitor visitor,
                                                            @RequestBody AdminReservationRequest adminReservationRequest) {
        ReservationCreateResponse reservationCreateResponse = adminReservationService.create(visitor,
                adminReservationRequest);
        return ResponseEntity.created(URI.create("/admin/reservation")).body(reservationCreateResponse);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> findByFilter(
            @RequestParam(required = false) Long themeId, @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) LocalDate dateFrom, @RequestParam(required = false) LocalDate dateTo) {
        List<ReservationResponse> reservationResponses = adminReservationService.findByFilter(themeId, memberId,
                dateFrom, dateTo);
        return ResponseEntity.ok(reservationResponses);
    }
}
