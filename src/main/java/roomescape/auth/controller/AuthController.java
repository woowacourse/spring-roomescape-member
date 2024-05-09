package roomescape.auth.controller;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.service.AuthService;

@RestController
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
    String token = authService.createUser(request);
    return ResponseEntity.ok()
        .header(SET_COOKIE, "token=" + token)
        .build();
  }
}
