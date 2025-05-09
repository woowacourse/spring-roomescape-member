package roomescape.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Tag(name = "사용자 페이지 컨트롤러", description = "웹 페이지 제공 컨트롤러")
public class UserPageController {

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

    @GetMapping("/login")
    @Operation(summary = "로그인 페이지", description = "로그인 화면을 제공합니다.")
    public String getLoginPage() {
        return "login";
    }
}
