package roomescape.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminWebController {

  @GetMapping
  public String getAdminPage() {
    return "/admin/index";
  }

  @GetMapping("/reservation")
  public String getReservationPage() {
    return "/admin/reservation-new";
  }

  @GetMapping("/time")
  public String getTimePage() {
    return "/admin/time";
  }

  @GetMapping("/theme")
  public String getThemePage() {
    return "/admin/theme";
  }
}
