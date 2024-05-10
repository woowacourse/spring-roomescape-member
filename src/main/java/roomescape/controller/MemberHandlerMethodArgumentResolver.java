package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.controller.request.LoginMemberInformation;
import roomescape.service.MemberAuthService;
import roomescape.service.response.MemberAppResponse;

@Component
public class MemberHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private MemberAuthService memberAuthService;

    public MemberHandlerMethodArgumentResolver(MemberAuthService memberAuthService) {
        this.memberAuthService = memberAuthService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Auth.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw new IllegalArgumentException("요청이 없습니다. 다시 로그인 해주세요.");
        }
        if (request.getCookies() == null) {
            throw new IllegalArgumentException("쿠키가 없습니다. 다시 로그인 해주세요.");
        }
        String token = extractTokenFromCookie(request.getCookies());
        MemberAppResponse appResponse = memberAuthService.findMemberByToken(token);

        return new LoginMemberInformation(appResponse.name(), appResponse.id());
    }

    private String extractTokenFromCookie(Cookie[] cookies) {

        Optional<String> token = Arrays.stream(cookies)
            .filter(cookie -> cookie.getName().equals("token"))
            .map(Cookie::getValue)
            .findFirst();

        return token.orElseThrow(() -> new IllegalArgumentException("토큰이 존재하지 않습니다."));
    }
}