package roomescape.controller.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import javax.naming.AuthenticationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.service.MemberService;

@RestController
@RequestMapping("/login")
public class LoginController {

    private static final String TOKEN = "token";
    private final MemberService memberService;

    public LoginController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response)
            throws AuthenticationException {
        String token = memberService.getLoginToken(loginRequest);

        Cookie cookie = new Cookie(TOKEN, token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        return ResponseEntity.ok().headers(headers).build();
    }

    @GetMapping("/check")
    public MemberResponse getLoginMember(MemberResponse memberResponse) {
        return memberResponse;
    }
}
