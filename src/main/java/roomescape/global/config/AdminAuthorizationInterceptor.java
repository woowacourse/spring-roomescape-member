package roomescape.global.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.domain.Member;
import roomescape.auth.service.AuthService;

@Component
public class AdminAuthorizationInterceptor implements HandlerInterceptor {

  private final AuthService authService;

  public AdminAuthorizationInterceptor(final AuthService authService) {
    this.authService = authService;
  }

  @Override
  public boolean preHandle(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final Object handler
  ) {
    final Cookie[] cookies = request.getCookies();
    final String token = extractTokenFromCookie(cookies);

    final Member member = authService.findMember(token);
    if (!member.isAdmin()) {
      throw new IllegalArgumentException("어드민 권한이 없습니다.");
    }
    return true;
  }

  private String extractTokenFromCookie(final Cookie[] cookies) {
    for (final Cookie cookie : cookies) {
      if (cookie.getName().equals("token")) {
        return cookie.getValue();
      }
    }
    throw new IllegalArgumentException("요청 쿠키에 토큰이 없습니다.");
  }
}
