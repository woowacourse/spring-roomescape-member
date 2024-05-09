package roomescape.auth.controller;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.CheckLoginResponse;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.service.AuthService;

@RestController
@RequestMapping("/login")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping
  public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
    String token = authService.createUser(request);
    return ResponseEntity.ok()
        .header(SET_COOKIE, "token=" + token)
        .build();
  }

  @GetMapping("/check")
  public ResponseEntity<CheckLoginResponse> checkLogin(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();

    String name = authService.checkLogin(extractTokenFromCookie(cookies));
    CheckLoginResponse response = CheckLoginResponse.from(name);
    return ResponseEntity.ok(response);
  }

  private String extractTokenFromCookie(Cookie[] cookies) {
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("token")) {
        return cookie.getValue();
      }
    }
    throw new IllegalArgumentException("요청 쿠키에 토큰이 없습니다.");
  }
}
