package roomescape.controller.page.admin;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import roomescape.dto.response.ReservationResponse;
import roomescape.repository.reservation.ReservationRepository;

@Controller
public class AdminReservationPageController {

    private final ReservationRepository reservationRepository;

    public AdminReservationPageController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/admin")
    public String getAdminPage() {
        return "/admin/index";
    }

    @GetMapping("/admin/reservation")
    public String getReservationPage(Model model) {
        List<ReservationResponse> reservationResponses = reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
        model.addAttribute("reservationResponses", reservationResponses);
        return "/admin/reservation-new";
    }
}
