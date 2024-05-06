package roomescape.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.admin.dto.ReservationRequest;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.ReservationTimeService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final ReservationService reservationService;
    private final ReservationTimeService reservationTimeService;

    public AdminController(ReservationService reservationService, ReservationTimeService reservationTimeService) {
        this.reservationService = reservationService;
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public String admin() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String reservation(Model model) {
        model.addAttribute("reservations", reservationService.findMemberReservations());
        return "admin/reservation-new";
    }

    @GetMapping("/time")
    public String time(Model model) {
        model.addAttribute("reservationTimes", reservationTimeService.findAll());
        return "admin/time";
    }

    @GetMapping("/theme")
    public String theme() {
        return "admin/theme";
    }

    @PostMapping("/reservations")
    public ResponseEntity<Long> create(@RequestBody @Valid ReservationRequest reservationRequest) {
        long id = reservationService.create(reservationRequest);
        return ResponseEntity.created(URI.create("/admin/reservations/" + id)).body(id);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") @Min(1) long reservationId) {
        reservationService.delete(reservationId);
        return ResponseEntity.noContent().build();
    }
}
