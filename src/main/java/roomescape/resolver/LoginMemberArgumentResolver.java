package roomescape.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;
import roomescape.domain.LoginMember;
import roomescape.exception.InvalidCredentialsException;
import roomescape.exception.InvalidTokenException;
import roomescape.service.MemberService;
import roomescape.util.CookieExtractor;
import roomescape.util.CookieKeys;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    private final CookieExtractor authorizationExtractor;

    public LoginMemberArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
        this.authorizationExtractor = new CookieExtractor();
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        try {
            String token = authorizationExtractor.extract(request, CookieKeys.TOKEN);
            return memberService.findMemberByToken(token);
        } catch (InvalidCredentialsException | InvalidTokenException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
