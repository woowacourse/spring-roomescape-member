package roomescape.ui;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.controller.response.UserResponse;
import roomescape.domain.Member;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.service.MemberService;

@Component
public class MemberConverterArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberConverterArgumentResolver(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MemberConverter.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie[] cookies = request.getCookies();
        String token = jwtTokenProvider.extractTokenFromCookie(cookies);
        String email = jwtTokenProvider.getEmailByToken(token);
        UserResponse userResponse = memberService.findByEmail(email);
        return new Member(userResponse.id(), userResponse.name(), userResponse.email(), userResponse.password(),
                userResponse.role());
    }
}
