package roomescape.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    // 1. 사용자 메인 페이지 (인기 테마 & 전체 테마)
    @GetMapping("/")
    public String indexPage() {
        return "user/index";
    }

    // 2. 사용자 예약 페이지
    @GetMapping("/reservations")
    public String userReservationPage() {

        return "user/reservations";
    }

    // 3. 관리자 예약 관리 페이지
    @GetMapping("/admin/reservations")
    public String adminReservationPage() {
        return "admin/reservations";
    }

    // 4. 관리자 테마 관리 페이지
    @GetMapping("/admin/themes")
    public String adminThemePage() {
        return "admin/themes";
    }
}