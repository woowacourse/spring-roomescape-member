package roomescape.service.auth;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dto.auth.LoginRequest;
import roomescape.entity.member.Member;
import roomescape.exceptions.auth.AuthorizationException;
import roomescape.infrastructure.jwt.JwtCookieResolver;
import roomescape.infrastructure.jwt.JwtTokenProvider;
import roomescape.infrastructure.member.MemberInfo;
import roomescape.repository.member.MemberRepository;

@Service
@Transactional(readOnly = true)
public class AuthenticationService {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtCookieResolver jwtCookieResolver;
    private final MemberRepository memberRepository;

    public AuthenticationService(JwtTokenProvider jwtTokenProvider,
                                 JwtCookieResolver jwtCookieResolver,
                                 MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtCookieResolver = jwtCookieResolver;
        this.memberRepository = memberRepository;
    }


    public String createToken(LoginRequest request) {
        String email = request.email();
        String password = request.password();
        if (!memberRepository.existsByEmailAndPassword(email, password)) {
            throw new AuthorizationException("회원 정보를 찾을 수 없습니다.");
        }
        Member member = memberRepository.findByEmail(email);
        return jwtTokenProvider.createToken(member);
    }

    public Cookie createCookie(String token) {
        return jwtCookieResolver.createCookie(token);
    }

    public String findNameByToken(String token) {
        MemberInfo info = jwtTokenProvider.resolveToken(token);
        Member member = memberRepository.findById(info.id());
        return member.getName();
    }
}
