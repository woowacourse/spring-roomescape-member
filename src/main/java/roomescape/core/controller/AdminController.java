package roomescape.core.controller;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.core.dto.reservation.ReservationRequest;
import roomescape.core.dto.reservation.ReservationResponse;
import roomescape.core.service.ReservationService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final ReservationService adminReservationService;

    public AdminController(final ReservationService adminReservationService) {
        this.adminReservationService = adminReservationService;
    }

    @GetMapping
    public String admin() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String reservation() {
        return "admin/reservation-new";
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservationAsAdmin(
            @Valid @RequestBody final ReservationRequest request) {
        final ReservationResponse result = adminReservationService.create(request);
        return ResponseEntity.created(URI.create("/reservations/" + result.getId()))
                .body(result);
    }

    @GetMapping("/time")
    public String time() {
        return "admin/time";
    }

    @GetMapping("/theme")
    public String theme() {
        return "admin/theme";
    }
}
