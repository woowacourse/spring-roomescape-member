package roomescape.auth.infrastructure;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.dto.LoginMember;
import roomescape.error.NotFoundException;
import roomescape.error.UnauthorizedException;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

@Component
@RequiredArgsConstructor
public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String TOKEN_COOKIE_NAME = "token";

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        String token = extractTokenFromCookies(request.getCookies());
        if (!StringUtils.hasText(token)) {
            throw new UnauthorizedException("토큰이 없습니다.");
        }

        final String subject = jwtTokenProvider.getSubject(token);
        final Long memberId = Long.valueOf(subject);

        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 멤버입니다."));

        return new LoginMember(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPassword(),
                member.getRole());
    }

    private String extractTokenFromCookies(final Cookie[] cookies) {
        if (cookies == null) {
            return "";
        }
        return Arrays.stream(cookies)
                .filter(c -> TOKEN_COOKIE_NAME.equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse("");
    }
}
