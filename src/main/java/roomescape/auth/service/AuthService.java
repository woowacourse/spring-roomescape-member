package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginRequest;
import roomescape.globar.infra.JwtTokenProvider;

@Service
public class AuthService {

  private final JwtTokenProvider jwtTokenProvider;

  public AuthService(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  public String createUser(LoginRequest request) {
    return jwtTokenProvider.createToken(request.email(), "어드민");
  }
}
