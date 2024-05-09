package roomescape.auth.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.application.JwtTokenProvider;
import roomescape.auth.dto.LoginMember;
import roomescape.exception.TokenNotExistException;
import roomescape.member.application.MemberService;
import roomescape.member.domain.Member;

import java.util.Arrays;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String TOKEN_COOKIE_KEY = "token";

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginMemberArgumentResolver(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = extractToken(servletRequest);
        jwtTokenProvider.validateToken(token);
        String email = jwtTokenProvider.getPayload(token);
        Member member = memberService.findByEmail(email);
        return new LoginMember(member.getId(), member.getEmail(), member.getName(), member.getPassword());
    }

    private String extractToken(HttpServletRequest servletRequest) {
        Cookie[] cookies = servletRequest.getCookies();
        if (cookies == null) {
            throw new TokenNotExistException();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(TOKEN_COOKIE_KEY))
                .findAny()
                .orElseThrow(TokenNotExistException::new)
                .getValue();
    }
}
