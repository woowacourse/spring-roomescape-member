package roomescape.controller.roomescape.admin;

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
import roomescape.controller.dto.request.AdminReservationSaveRequest;
import roomescape.controller.dto.response.AdminReservationResponse;
import roomescape.service.roomescape.admin.AdminReservationService;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    private final AdminReservationService adminReservationService;

    public AdminReservationController(final AdminReservationService adminReservationService) {
        this.adminReservationService = adminReservationService;
    }

    @PostMapping
    public ResponseEntity<AdminReservationResponse> save(
            @RequestBody AdminReservationSaveRequest reservationSaveRequest
    ) {
        AdminReservationResponse reservationResponse = adminReservationService.save(reservationSaveRequest);
        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @GetMapping
    public ResponseEntity<List<AdminReservationResponse>> getByFilter(
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) Long themeId,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo
    ) {
        return ResponseEntity.ok(
                adminReservationService.getByFilter(memberId, themeId, dateFrom, dateTo)
        );
    }
}
