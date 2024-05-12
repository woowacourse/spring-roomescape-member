package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.dto.request.ReservationAdminCreateRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

import java.net.URI;

@Controller
@RequestMapping("admin")
public class AdminPageController {

    private final ReservationService reservationService;

    public AdminPageController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public String showAdminIndexPage() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String showReservationPage() {
        return "admin/reservation-new";
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createAdminReservation(
            @RequestBody ReservationAdminCreateRequest reservationAdminCreateRequest) {
        ReservationResponse reservationResponse = reservationService.createAdminReservation(reservationAdminCreateRequest);
        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @GetMapping("/time")
    public String showReservationTimePage() {
        return "admin/time";
    }

    @GetMapping("/theme")
    public String showThemePage() {
        return "admin/theme";
    }
}
