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

  public AuthenticationPrincipalArgumentResolver(AuthService authService) {
    this.authService = authService;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

    HttpServletRequest request
        = (HttpServletRequest) webRequest.getNativeRequest();

    Cookie[] cookies = request.getCookies();
    String token = extractTokenFromCookie(cookies);

    return authService.findMember(token);
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
