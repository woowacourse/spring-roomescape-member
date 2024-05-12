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
import roomescape.service.dto.SignupRequest;
import roomescape.service.dto.SignupResponse;
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

    public Cookie login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(UnauthorizedEmailException::new);
        if (!member.getPassword().equals(request.getPassword())) {
            throw new UnauthorizedPasswordException();
        }
        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRole());
        return cookieExtractor.createCookie(token);
    }

    public LoginCheckResponse loginCheck(Member member) {
        return new LoginCheckResponse(member);
    }

    public Cookie logout() {
        return cookieExtractor.deleteCookie();
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

    public SignupResponse signup(SignupRequest request) {
        Member member = request.toMember(MemberRole.USER);
        Member savedMember = memberRepository.save(member);
        return new SignupResponse(savedMember);
    }
}
