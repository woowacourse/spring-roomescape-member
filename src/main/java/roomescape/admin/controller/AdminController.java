package roomescape.admin.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.admin.dto.request.AdminReservationCreateRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.service.ReservationService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ReservationService reservationService;

    public AdminController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public String getHomepage() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String getReservationPage() {
        return "admin/reservation-new";
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody AdminReservationCreateRequest request
    ) {
        ReservationResponse response = reservationService.create(request);
        URI location = URI.create("http://localhost:8080/reservations/" + response.id());
        return ResponseEntity.created(location).body(response);
    }


    @GetMapping("/time")
    public String getTimePage() {
        return "admin/time";
    }

    @GetMapping("/theme")
    public String getThemePage() {
        return "admin/theme";
    }
}
