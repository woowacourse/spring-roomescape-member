package roomescape.controller.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.infrastructure.CookieManager;
import roomescape.infrastructure.auth.Token;
import roomescape.service.LoginService;
import roomescape.service.MemberService;

@RestController
public class LoginController {
    private final CookieManager cookieManager; // TODO 세가지 객체를 감싸는 하나의 서비스 생각해보기
    private final LoginService loginService;
    private final MemberService memberService;

    public LoginController(
            final CookieManager cookieManager,
            final LoginService loginService,
            final MemberService memberService
    ) {
        this.cookieManager = cookieManager;
        this.loginService = loginService;
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletResponse response
    ) {
        Token token = loginService.login(
                memberService.findMemberByEmailAndPassword(request.email(), request.password()),
                request.email(),
                request.password()
        );
        response.addCookie(cookieManager.generate(token));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        Token token = cookieManager.getToken(request.getCookies());
        Long memberId = loginService.findMemberIdByToken(token);
        MemberResponse memberResponse = new MemberResponse(memberService.findMemberById(memberId));
        return ResponseEntity.ok(memberResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = cookieManager.makeResetCookie(request.getCookies());
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
