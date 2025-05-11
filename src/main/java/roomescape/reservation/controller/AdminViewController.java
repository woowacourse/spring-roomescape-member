package roomescape.reservation.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.reservation.dto.request.AdminReservationCreateRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.service.ReservationService;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    private final ReservationService reservationService;

    public AdminViewController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public String admin() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String adminReservation() {
        return "admin/reservation-new";
    }

    @GetMapping("/time")
    public String adminTime() {
        return "admin/time";
    }

    @GetMapping("/theme")
    public String adminTheme() {
        return "admin/theme";
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservationByAdmin(
            @RequestBody AdminReservationCreateRequest adminReservationCreateRequest) {
        ReservationResponse newReservation = reservationService.addReservation(adminReservationCreateRequest);
        return ResponseEntity.created(URI.create("/reservations/" + newReservation.id())).body(newReservation);
    }
}
