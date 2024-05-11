package roomescape.controller.page.admin;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.dto.response.ReservationResponse;
import roomescape.repository.reservation.ReservationRepository;

@Controller
@RequestMapping("/admin")
public class AdminReservationPageController {

    private final ReservationRepository reservationRepository;

    public AdminReservationPageController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @GetMapping
    public String getAdminHome() {
        return "/admin/index";
    }

    @GetMapping("/time")
    public String getAdminTimePage() {
        return "/admin/time";
    }

    @GetMapping("/theme")
    public String getAdminThemePage() {
        return "/admin/theme";
    }

    @GetMapping("/reservation")
    public String getReservationPage(Model model) {
        List<ReservationResponse> reservationResponses = reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::new)
                .toList();
        model.addAttribute("reservationResponses", reservationResponses);
        return "/admin/reservation-new";
    }
}
