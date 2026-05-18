package roomescape.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;
import roomescape.service.ThemeService;

@Controller
public class AdminViewController {

    private final ReservationService reservationService;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public AdminViewController(ReservationService reservationService,
                               ReservationTimeService reservationTimeService,
                               ThemeService themeService) {
        this.reservationService = reservationService;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
    }

    @GetMapping("/admin")
    public String index() {
        return "admin/index";
    }

    @GetMapping("/admin/reservation")
    public String reservation(Model model) {
        model.addAttribute("reservations", ReservationResponse.from(reservationService.findAllReservations()));
        model.addAttribute("themes", ThemeResponse.from(themeService.getThemes()));
        model.addAttribute("times", ReservationTimeResponse.from(reservationTimeService.findAllReservationTimes()));

        return "admin/reservation";
    }

    @GetMapping("/admin/time")
    public String time(Model model) {
        model.addAttribute("times", ReservationTimeResponse.from(reservationTimeService.findAllReservationTimes()));

        return "admin/time";
    }

    @GetMapping("/admin/theme")
    public String theme(Model model) {
        model.addAttribute("themes", ThemeResponse.from(themeService.getThemes()));

        return "admin/theme";
    }
}
