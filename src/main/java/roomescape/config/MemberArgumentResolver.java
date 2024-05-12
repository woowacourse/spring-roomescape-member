package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.exception.AuthorizationException;
import roomescape.member.dto.MemberProfileInfo;
import roomescape.member.security.service.MemberAuthService;

@Component
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberAuthService memberAuthService;

    public MemberArgumentResolver(MemberAuthService memberAuthService) {
        this.memberAuthService = memberAuthService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType()
                .equals(MemberProfileInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws AuthorizationException {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        return memberAuthService.extractPayload(Objects.requireNonNull(request)
                .getCookies());
    }

}
