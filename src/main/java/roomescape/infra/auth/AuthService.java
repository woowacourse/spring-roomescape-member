package roomescape.infra.auth;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.dto.NameResponse;
import roomescape.model.user.Member;
import roomescape.model.user.Role;
import roomescape.repository.MemberRepository;

@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProcessor jwtTokenProcessor;

    public AuthService(MemberRepository memberRepository, JwtTokenProcessor jwtTokenProcessor) {
        this.memberRepository = memberRepository;
        this.jwtTokenProcessor = jwtTokenProcessor;
    }

    public Cookie login(String email, String password) {
        Member member = memberRepository.login(email, password);
        String token = jwtTokenProcessor.createToken(member.getEmail(), member.getRole());
        Cookie cookie = new Cookie("loginToken", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public NameResponse checkLogin(Cookie[] cookies) {
        String loginToken = extractTokenFromCookies(cookies);
        validateLoginToken(loginToken);
        String name = memberRepository.findNameByEmail(getPayload(loginToken)).getValue();
        return new NameResponse(name);
    }

    public String extractTokenFromCookies(Cookie[] cookies) {
        return jwtTokenProcessor.extractTokenFromCookies(cookies);
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Member getMemberFromCookies(Cookie[] cookies) {
        return getMemberByEmail(getPayload(extractTokenFromCookies(cookies)));
    }

    public void validateLoginToken(String loginToken) {
        if (jwtTokenProcessor.validateToken(loginToken)) {
            return;
        }
        throw new JwtException("Invalid token");
    }

    private String getPayload(String token) {
        return jwtTokenProcessor.getPayload(token);
    }

    public Role getRole(String token) {
        return jwtTokenProcessor.getRole(token);
    }
}
