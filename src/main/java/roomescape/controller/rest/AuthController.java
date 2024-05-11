package roomescape.controller.rest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Arrays;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.auth.LoginRequest;
import roomescape.dto.auth.LoginResponse;
import roomescape.global.exception.ApplicationException;
import roomescape.service.AuthService;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        String accessToken = authService.login(loginRequest);

        // TODO: cookie 생성 로직이 controller에 있는게 맞는지 생각해보기
        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        // TODO: 응답 어떻게 주는게 좋은지 생각해보기
        return ResponseEntity.ok().build();
    }

    // TODO: 테스트 어떻게 하지?
    @GetMapping("/check")
    public ResponseEntity<LoginResponse> readLoggedInMemberInfo(HttpServletRequest request) {
        // TODO: 책임 소재 고민
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            // TODO: 409 Conflict 터뜨리면 안될듯?
            throw new ApplicationException("쿠키가 존재하지 않습니다.");
        }

        String token = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .orElseThrow(() -> new ApplicationException("쿠키에 토큰 정보가 존재하지 않습니다."))
                .getValue();

        // TODO: 환경 변수 분리
        String secretKey = "나는 신이다.나는 신이다.나는 신이다.나는 신이다.나는 신이다.나는 신이다.나는 신이다.나는 신이다.나는 신이다.나는 신이다.나는 신이다.";

        // TODO: 책임 소재 고민
        Long memberId = Long.valueOf(Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());

        return ResponseEntity.ok(authService.findLoggedInMember(memberId));
    }
}
