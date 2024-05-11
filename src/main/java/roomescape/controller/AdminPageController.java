package roomescape.controller;

import java.net.URI;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.function.EntityResponse;
import roomescape.domain.member.Member;
import roomescape.dto.AdminReservationRequest;
import roomescape.service.ReservationService;

@Controller
@RequestMapping("/admin")
public class AdminPageController {
    private final ReservationService reservationService;

    public AdminPageController(final ReservationService reservationService) {
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
    public ResponseEntity<Object> createReservation(@RequestBody AdminReservationRequest adminReservationRequest, Member member) {
        reservationService.addReservation(adminReservationRequest, member);
        Long id = null;
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }
}
