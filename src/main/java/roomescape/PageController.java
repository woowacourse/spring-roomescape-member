package roomescape;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Spring Boot는 static/index.html을 "/" 로 자동 서빙하지만,
 * 하위 디렉터리 인덱스(예: /admin/ → static/admin/index.html)는
 * 자동 처리되지 않으므로 최소한의 포워딩만 담당한다.
 */
@Controller
public class PageController {

    @GetMapping({"/admin", "/admin/"})
    public String adminHome() {
        return "forward:/admin/index.html";
    }
}
