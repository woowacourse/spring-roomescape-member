package roomescape.globar.config;

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

  public AdminAuthorizationInterceptor(AuthService authService) {
    this.authService = authService;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    Cookie[] cookies = request.getCookies();
    String token = extractTokenFromCookie(cookies);

    Member member = authService.findMember(token);
    if (!member.isAdmin()) {
      throw new IllegalArgumentException("어드민 권한이 없습니다.");
    }
    return true;
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
