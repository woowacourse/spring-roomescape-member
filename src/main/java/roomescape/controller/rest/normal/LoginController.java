package roomescape.controller.rest.normal;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.member.Member;
import roomescape.service.LoginService;
import roomescape.service.response.LoginCheckResponse;
import roomescape.service.request.LoginRequest;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(final LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpSession session) {
        Member member = loginService.login(request);
        session.setAttribute("LOGIN_MEMBER", member);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<LoginCheckResponse> loginCheck(HttpSession httpSession) {
        Member member = (Member) httpSession.getAttribute("LOGIN_MEMBER");
        LoginCheckResponse loginCheckResponse = new LoginCheckResponse(member.getName().name());
        return ResponseEntity.ok(loginCheckResponse);
    }
}
