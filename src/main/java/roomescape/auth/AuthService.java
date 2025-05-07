package roomescape.auth;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.controller.request.LoginMemberInfo;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;
import roomescape.exception.UnAuthorizedException;
import roomescape.service.result.LoginMemberResult;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public String createToken(LoginMemberResult loginMemberResult) {
        return jwtTokenProvider.createToken(loginMemberResult);
    }

    public Long extractSubjectFromToken(Cookie[] cookies) {
        String token = jwtTokenProvider.extractTokenFromCookie(cookies);
        if (token == null) {
            throw new UnAuthorizedException();
        }

        return jwtTokenProvider.extractIdFromToken(token);
    }

    public LoginMemberInfo findMemberByToken(final String token) {
        if (token == null || token.isBlank()) {
            throw new UnAuthorizedException();
        }

        Long id = jwtTokenProvider.extractIdFromToken(token);
        Member member = memberRepository.findById(id).orElseThrow(UnAuthorizedException::new);

        return LoginMemberInfo.of(member.getName());
    }
}
