package roomescape.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.reservation.AdminReservationCreateRequest;
import roomescape.dto.reservation.ReservationFilterRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.service.AdminReservationService;

@RestController
public class AdminReservationController {

    private static final ZoneId KST_ZONE = ZoneId.of("Asia/Seoul");

    private final AdminReservationService adminReservationService;

    public AdminReservationController(AdminReservationService adminReservationService) {
        this.adminReservationService = adminReservationService;
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> create(@RequestBody AdminReservationCreateRequest request) {
        LocalDateTime now = LocalDateTime.now(KST_ZONE);
        return ResponseEntity.created(URI.create("/admin/reservations"))
                .body(adminReservationService.add(request, now));
    }

    @GetMapping(value = "/admin/reservations")
    public ResponseEntity<List<ReservationResponse>> getFiltered(ReservationFilterRequest request) {
        return ResponseEntity.ok(adminReservationService.findFiltered(request));
    }
}
