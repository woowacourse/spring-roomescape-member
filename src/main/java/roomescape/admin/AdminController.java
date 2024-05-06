package roomescape.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        model.addAttribute("reservations", reservationService.findAllReservations());
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
}
