package roomescape.controller.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.exception.UnauthorizedException;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.LoginedMemberResponse;
import roomescape.dto.response.MemberResponse;
import roomescape.service.AuthService;
import roomescape.service.MemberService;
import roomescape.util.CookieUtil;

@RestController
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;

    public AuthController(AuthService authService, MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponse> login(
            @RequestBody @Valid LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        LoginedMemberResponse loginedMemberResponse = authService.createToken(loginRequest);
        String token = loginedMemberResponse.token();
        MemberResponse memberResponse = loginedMemberResponse.memberResponse();

        CookieUtil.setTokenCookie(response, token);

        return ResponseEntity.ok(memberResponse);
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        String token = CookieUtil.extractTokenFromCookie(request);

        if (token == null) {
            throw new UnauthorizedException();
        }

        Long id = authService.getMemberIdByToken(token);
        MemberResponse memberResponse = memberService.getById(id);

        return ResponseEntity.ok(memberResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        CookieUtil.clearTokenCookie(response);

        return ResponseEntity.ok().build();
    }
}
