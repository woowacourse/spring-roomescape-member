package roomescape.auth.resolver;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.dto.LoginMember;
import roomescape.auth.exception.UnauthorizedException;
import roomescape.global.security.jwt.JwtTokenExtractor;
import roomescape.global.security.jwt.JwtTokenProvider;
import roomescape.member.entity.Member;
import roomescape.member.service.MemberService;

import java.util.NoSuchElementException;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenExtractor jwtTokenExtractor;

    public LoginMemberArgumentResolver(MemberService memberService, JwtTokenProvider jwtTokenProvider, JwtTokenExtractor jwtTokenExtractor) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtTokenExtractor = jwtTokenExtractor;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = jwtTokenExtractor.extractToken(request);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }

        try {
            Long memberId = jwtTokenProvider.getMemberId(token);
            Member member = memberService.findById(memberId);
            return new LoginMember(member.getId(), member.getRole());
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("토큰이 만료되었습니다.");
        } catch (JwtException e) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        } catch (NoSuchElementException e) {
            throw new UnauthorizedException("존재하지 않는 사용자입니다.");
        }
    }
}
