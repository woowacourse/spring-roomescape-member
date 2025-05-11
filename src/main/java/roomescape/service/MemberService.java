package roomescape.service;

import jakarta.servlet.http.Cookie;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.TokenResponse;
import roomescape.infra.JwtTokenProcessor;
import roomescape.model.user.Member;
import roomescape.model.user.Name;
import roomescape.repository.MemberRepository;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProcessor jwtTokenProcessor;

    public MemberService(MemberRepository memberRepository, JwtTokenProcessor jwtTokenProcessor) {
        this.memberRepository = memberRepository;
        this.jwtTokenProcessor = jwtTokenProcessor;
    }

    public Member login(String email, String password) {
        Member member = memberRepository.login(email, password);
        System.out.println(member);
        return member;
    }

    public TokenResponse createToken(String payload) {
        String accessToken = jwtTokenProcessor.createToken(payload);
        return new TokenResponse(accessToken);
    }

    public Cookie createCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public String extractUserEmailFromCookies(Cookie[] cookies) {
        return jwtTokenProcessor.extractUserEmailFromCookie(cookies);
    }

    public Name getUserNameByUserEmail(String userEmail) {
        return memberRepository.findUserNameByUserEmail(userEmail);
    }

    public Name getUserNameByUserId(Long userId) {
        return memberRepository.findUserNameByUserId(userId);
    }

    public List<Member> getAllUsers() {
        return memberRepository.findAllUsers();
    }
}
