package roomescape.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    // 1. 사용자 메인 페이지 (인기 테마 & 전체 테마)
    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    // 2. 사용자 예약 페이지
    @GetMapping("/reservations")
    public String userReservationPage() {

        return "user/reservations";
    }

    // 3. 관리자 예약 관리 페이지
    @GetMapping("/admin")
    public String adminReservationPage() {
        return "admin/management";
    }
}
