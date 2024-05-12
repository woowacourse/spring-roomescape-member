package roomescape.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;
import roomescape.domain.MemberRole;
import roomescape.exception.auth.InvalidTokenException;
import roomescape.exception.auth.UnauthorizedEmailException;
import roomescape.exception.auth.UnauthorizedPasswordException;
import roomescape.service.dto.LoginCheckResponse;
import roomescape.service.dto.LoginRequest;
import roomescape.service.helper.CookieExtractor;
import roomescape.service.helper.JwtTokenProvider;

@Service
public class AuthService {
    private final CookieExtractor cookieExtractor;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(CookieExtractor cookieExtractor,
                       JwtTokenProvider jwtTokenProvider,
                       MemberRepository memberRepository) {
        this.cookieExtractor = cookieExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public String login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(UnauthorizedEmailException::new);
        if (!member.getPassword().equals(request.getPassword())) {
            throw new UnauthorizedPasswordException();
        }
        return jwtTokenProvider.createToken(member.getEmail(), member.getRole());
    }

    public LoginCheckResponse loginCheck(Member member) {
        return new LoginCheckResponse(member);
    }

    public MemberRole findMemberRoleByCookie(Cookie[] cookies) {
        String token = cookieExtractor.getToken(cookies);
        return jwtTokenProvider.getMemberRole(token);
    }

    public Member findMemberByCookie(Cookie[] cookies) {
        String token = cookieExtractor.getToken(cookies);
        String email = jwtTokenProvider.getMemberEmail(token);
        return findMemberByEmail(email);
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(InvalidTokenException::new);
    }
}
