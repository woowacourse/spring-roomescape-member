package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.business.service.member.MemberService;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String ACCESS_TOKEN_COOKIE_NAME = "token";

    private final MemberService memberService;

    public LoginMemberArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        String token = getAccessToken(webRequest);
        return memberService.getMemberFromToken(token);
    }

    private String getAccessToken(NativeWebRequest webRequest) {
        HttpServletRequest httpServletRequest = getHttpServletRequest(webRequest);
        String accessToken = getAccessTokenFromCookies(httpServletRequest.getCookies());
        if (accessToken == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }
        return accessToken;
    }

    private HttpServletRequest getHttpServletRequest(NativeWebRequest webRequest) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw new IllegalStateException("개발자에게 문의하세요.");
        }
        return request;
    }

    private String getAccessTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        return Arrays.stream(cookies)
                .filter(cookie -> ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
