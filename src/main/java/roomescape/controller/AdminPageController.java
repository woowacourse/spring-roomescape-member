package roomescape.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.controller.request.ReservationRequest2;
import roomescape.controller.response.ReservationResponse;
import roomescape.service.ReservationService;

@Controller
@RequestMapping("/admin")
public class AdminPageController {
    private final ReservationService reservationService;

    public AdminPageController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public String home() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String reservation() {
        return "admin/reservation-new";
    }

    @GetMapping("/time")
    public String time() {
        return "admin/time";
    }

    @GetMapping("/theme")
    public String theme() {
        return "admin/theme";
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> reservationPlus(@RequestBody ReservationRequest2 reservationRequest2) {
        ReservationResponse reservationResponse = reservationService.findMember(reservationRequest2);
        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id())).body(reservationResponse);
    }
}
