package roomescape.globar.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.service.AuthService;

@Component
public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

  private final AuthService authService;

  public AuthenticationPrincipalArgumentResolver(final AuthService authService) {
    this.authService = authService;
  }

  @Override
  public boolean supportsParameter(final MethodParameter parameter) {
    return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
  }

  @Override
  public Object resolveArgument(final MethodParameter parameter,
      final ModelAndViewContainer mavContainer,
      final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {

    final HttpServletRequest request
        = (HttpServletRequest) webRequest.getNativeRequest();

    final Cookie[] cookies = request.getCookies();
    final String token = extractTokenFromCookie(cookies);

    return authService.findMember(token);
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
