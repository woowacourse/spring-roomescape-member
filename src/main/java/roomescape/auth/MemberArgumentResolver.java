package roomescape.auth;

import javax.naming.AuthenticationException;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import roomescape.auth.util.LoginTokenParser;
import roomescape.exception.InvalidTokenException;
import roomescape.repository.MemberRepository;

@Component
@RequiredArgsConstructor
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = LoginTokenParser.getLoginToken(request);
        RequestMember annotation = parameter.getParameterAnnotation(RequestMember.class);

        if (token == null && !annotation.required()) {
            return null;
        }
        try {
            Long memberId = Long.valueOf(jwtProvider.getPayload(token));
            return memberRepository.getById(memberId);
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException(e);
        } catch (IllegalArgumentException e) {
            throw new AuthenticationException("로그인 정보가 잘못되었습니다.");
        }
    }
}
