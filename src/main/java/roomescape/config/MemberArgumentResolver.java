package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.business.domain.auth.JwtTokenProvider;
import roomescape.presentation.exception.UnauthorizedException;
import roomescape.repository.MemberRepository;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberArgumentResolver(final MemberRepository memberRepository, final JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthMember.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory)
            throws Exception {
        final HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        final Cookie[] cookies = request.getCookies();
        final String token = getToken(cookies);
        final String payload = jwtTokenProvider.getPayload(token);
        try {
            final long id = Long.parseLong(payload);
            return memberRepository.findById(id)
                    .orElseThrow(() -> new UnauthorizedException("확인할 수 없는 사용자입니다."));
        } catch (ArithmeticException e) {
            throw new UnauthorizedException("올바르지 않은 토큰 정보입니다.");
        }
    }

    private static String getToken(final Cookie[] cookies) {
        if (cookies == null) {
            throw new UnauthorizedException("쿠키를 입력해 주세요.");
        }
        return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .orElseThrow(() -> new UnauthorizedException("토큰을 입력해주세요."))
                .getValue();
    }
}
