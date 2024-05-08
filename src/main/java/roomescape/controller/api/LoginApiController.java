package roomescape.controller.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.api.dto.request.MemberLoginRequest;
import roomescape.service.MemberService;
import roomescape.service.dto.input.MemberCreateInput;
import roomescape.service.dto.output.MemberLoginOutput;

@RestController
@RequestMapping("/login")
public class LoginApiController {
    private final MemberService memberService;

    public LoginApiController(final MemberService memberService) {
        this.memberService = memberService;
        memberService.createMember(new MemberCreateInput("어드민", "admin@email.com", "password"));
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody final MemberLoginRequest memberLoginRequest, final HttpServletResponse response) {
        final MemberLoginOutput output = memberService.loginMember(memberLoginRequest.toInput());
        initializeCookie(response, output);
        return ResponseEntity.ok()
                .build();
    }

    private void initializeCookie(final HttpServletResponse response, final MemberLoginOutput output) {
        final Cookie cookie = new Cookie("token", output.token());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
