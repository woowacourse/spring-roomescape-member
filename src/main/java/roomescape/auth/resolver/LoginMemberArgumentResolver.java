package roomescape.auth.resolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.dto.LoginMember;
import roomescape.auth.exception.UnauthorizedException;
import roomescape.common.security.jwt.JwtTokenProvider;
import roomescape.member.entity.Member;
import roomescape.member.service.MemberService;

import java.util.Arrays;
import java.util.Optional;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginMemberArgumentResolver(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals( LoginMember.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = Arrays.stream(Optional.ofNullable(request.getCookies())
                        .orElseThrow(() -> new UnauthorizedException("로그인이 필요합니다.")))
                .filter(cookie -> cookie.getName().equals("jwt_token"))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new UnauthorizedException("유효한 토큰이 존재하지 않습니다."));

        Long memberId = jwtTokenProvider.getMemberId(token);

        Member member = memberService.findById(memberId);

        return new LoginMember(member.getId(), member.getEmail(), member.getName(), member.getRole());
    }
}
