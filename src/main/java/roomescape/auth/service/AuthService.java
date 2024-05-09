package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.globar.infra.JwtTokenProvider;

@Service
public class AuthService {

  private final JwtTokenProvider jwtTokenProvider;

  public AuthService(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  public String createUser(String email) {
    return jwtTokenProvider.createToken(email);
  }
}
