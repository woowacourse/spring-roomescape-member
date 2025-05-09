package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.Member;
import roomescape.service.member.MemberServiceImpl;
import roomescape.util.CookieManager;
import roomescape.util.JwtTokenProvider;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberServiceImpl memberServiceImpl;

    public LoginMemberArgumentResolver(MemberServiceImpl memberServiceImpl) {
        this.memberServiceImpl = memberServiceImpl;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Cookie[] cookies = request.getCookies();
        String jwtToken = CookieManager.extractTokenFromCookies(cookies);
        Long id = JwtTokenProvider.findMemberIdByToken(jwtToken);
        Member member = memberServiceImpl.findMemberById(id);
        return member;
    }
}
