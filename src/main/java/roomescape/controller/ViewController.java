package roomescape.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Tag(name = "뷰 컨트롤러", description = "웹 페이지 뷰 제공 컨트롤러")
public class ViewController {

    @GetMapping("/admin")
    @Operation(summary = "관리자 홈 페이지", description = "관리자 대시보드 홈 화면을 제공합니다.")
    public String getAdminHomePage() {
        return "admin/index";
    }

    @GetMapping("/admin/reservation")
    @Operation(summary = "관리자 예약 관리 페이지", description = "관리자용 예약 관리 화면을 제공합니다.")
    public String getAdminReservationPage() {
        return "admin/reservation-new";
    }

    @GetMapping("/admin/time")
    @Operation(summary = "관리자 시간 관리 페이지", description = "관리자용 예약 시간 관리 화면을 제공합니다.")
    public String getAdminTimePage() {
        return "admin/time";
    }

    @GetMapping("/admin/theme")
    @Operation(summary = "관리자 테마 관리 페이지", description = "관리자용 테마 관리 화면을 제공합니다.")
    public String getAdminThemePage() {
        return "admin/theme";
    }

    @GetMapping("/reservation")
    @Operation(summary = "사용자 예약 페이지", description = "사용자용 예약 화면을 제공합니다.")
    public String getReservationPage() {
        return "reservation";
    }

    @GetMapping()
    @Operation(summary = "메인 페이지", description = "메인 홈 화면을 제공합니다.")
    public String getHomePage() {
        return "index";
    }
}
