package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.UserLoginRequest;
import roomescape.dto.response.CheckMemberResponse;
import roomescape.dto.response.TokenResponse;
import roomescape.service.MemberService;
import roomescape.config.MemberIdConverter;

@RestController
public class LoginController {
    private static final String TOKEN = "token";

    private final MemberService memberService;

    public LoginController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            HttpServletResponse response,
            @RequestBody @Valid UserLoginRequest userLoginRequest
    ) {
        TokenResponse tokenResponse = memberService.createToken(userLoginRequest);
        Cookie cookie = new Cookie(TOKEN, tokenResponse.token());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/login/check")
    public ResponseEntity<CheckMemberResponse> checkLogin(@MemberIdConverter Long memberId) {
        CheckMemberResponse checkMemberResponse = memberService.findById(memberId);
        return ResponseEntity.ok(checkMemberResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(TOKEN, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
