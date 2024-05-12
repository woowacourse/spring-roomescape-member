package roomescape.controller.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.api.dto.request.MemberLoginRequest;
import roomescape.controller.api.dto.response.TokenLoginResponse;
import roomescape.service.MemberService;
import roomescape.service.dto.output.MemberLoginOutput;
import roomescape.service.dto.output.TokenLoginOutput;

import java.util.Arrays;

@RestController
@RequestMapping("/login")
public class LoginApiController {
    private final MemberService memberService;

    public LoginApiController(final MemberService memberService) {
        this.memberService = memberService;
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

    @GetMapping("/check")
    public ResponseEntity<TokenLoginResponse> checkLogin(final HttpServletRequest request) {
        final String token = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName()
                        .equals("token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new IllegalArgumentException("토큰이 없습니다"));
        final TokenLoginOutput output = memberService.loginToken(token);
        return ResponseEntity.ok(TokenLoginResponse.toResponse(output));
    }
}
