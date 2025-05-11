package roomescape.controller.helper;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.controller.api.member.dto.MemberResponse;
import roomescape.service.MemberService;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;

    public LoginMemberArgumentResolver(final MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
        final HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        final String token = ControllerSupports.findCookieByKey(request, "token");
        final MemberResponse response = memberService.findByToken(token);
        return response.toEntity();
    }
}
