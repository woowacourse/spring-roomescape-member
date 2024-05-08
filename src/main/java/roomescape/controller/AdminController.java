package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.service.ReservationService;
import roomescape.service.dto.ReservationResponse;
import roomescape.service.dto.ReservationSaveRequest;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ReservationService reservationService;

    public AdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public String home() {
        return "/admin/index";
    }

    @GetMapping("/reservation")
    public String reservation() {
        return "/admin/reservation-new";
    }

    @GetMapping("/time")
    public String time() {
        return "/admin/time";
    }

    @GetMapping("/theme")
    public String theme() {
        return "/admin/theme";
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> saveReservation(
            @RequestBody @Valid ReservationSaveRequest reservationSaveRequest
    ) {
        ReservationResponse reservationResponse = reservationService.saveReservation(reservationSaveRequest);
        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }
}
