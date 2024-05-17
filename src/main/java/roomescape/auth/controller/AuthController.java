package roomescape.auth.controller;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.domain.Member;
import roomescape.auth.dto.CheckLoginResponse;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.service.AuthService;
import roomescape.global.config.AuthenticationPrincipal;

@RestController
@RequestMapping("/login")
public class AuthController {

  private final AuthService authService;

  public AuthController(final AuthService authService) {
    this.authService = authService;
  }

  @PostMapping
  public ResponseEntity<Void> login(@RequestBody final LoginRequest request) {
    final String token = authService.login(request);
    return ResponseEntity.ok()
        .header(SET_COOKIE, "token=" + token)
        .build();
  }

  @GetMapping("/check")
  public ResponseEntity<CheckLoginResponse> checkLogin(
      @AuthenticationPrincipal final Member member) {
    final CheckLoginResponse response = CheckLoginResponse.from(member.getName().getValue());
    return ResponseEntity.ok(response);
  }
}
