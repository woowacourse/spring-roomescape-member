package roomescape.controller.api;

import jakarta.servlet.http.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.LoginMember;
import roomescape.dto.auth.LoginRequestDto;
import roomescape.dto.auth.MemberResponseDto;
import roomescape.service.AuthService;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String token = authService.publishToken(loginRequestDto);
        Cookie cookie = createCookie(token);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    private Cookie createCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    @GetMapping("/check")
    public ResponseEntity<MemberResponseDto> checkLogin(LoginMember loginMember) {
        MemberResponseDto memberResponse = new MemberResponseDto(loginMember.name());
        return ResponseEntity.ok(memberResponse);
    }
}
