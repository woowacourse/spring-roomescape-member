package roomescape.global.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.global.exception.UnauthorizedException;
import roomescape.global.infra.JwtTokenProvider;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

  private final JwtTokenProvider jwtTokenProvider;

  public LoginCheckInterceptor(final JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  public boolean preHandle(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final Object handler
  ) {
    if (request.getCookies() == null) {
      throw new IllegalArgumentException("쿠키가 존재하지 않습니다.");
    }
    final Cookie[] cookies = request.getCookies();
    final String token = extractTokenFromCookie(cookies);

    if (!jwtTokenProvider.validateToken(token)) {
      throw new UnauthorizedException("접근 권한이 없습니다.");
    }
    return true;
  }

  private String extractTokenFromCookie(final Cookie[] cookies) {
    for (final Cookie cookie : cookies) {
      if (cookie.getName() != null && cookie.getName().equals("token")) {
        return cookie.getValue();
      }
    }
    throw new IllegalArgumentException("요청 쿠키에 토큰이 없습니다.");
  }
}
