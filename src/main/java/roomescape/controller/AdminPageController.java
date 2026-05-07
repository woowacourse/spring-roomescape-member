package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 관리자 화면 서빙 컨트롤러.
 * /admin GET 요청을 정적 HTML 파일로 포워딩합니다.
 */
@Controller
public class AdminPageController {

    @GetMapping("/admin")
    public String adminPage() {
        return "forward:/admin.html";
    }
}
