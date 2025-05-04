package roomescape.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Tag(name = "어드민 페이지 컨트롤러", description = "웹 페이지 제공 컨트롤러")
public class AdminPageController {

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
}
