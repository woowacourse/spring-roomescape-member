package roomescape.service;

import jakarta.servlet.http.Cookie;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.MemberRequest;
import roomescape.dto.TokenResponse;
import roomescape.infra.JwtTokenProcessor;
import roomescape.model.user.Member;
import roomescape.model.user.Name;
import roomescape.model.user.Role;
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

    public String extractEmailFromCookies(Cookie[] cookies) {
        return jwtTokenProcessor.extractUserEmailFromCookie(cookies);
    }

    public Name getNameByEmail(String email) {
        return memberRepository.findNameByEmail(email);
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    public Name getUserNameByUserId(Long userId) {
        return memberRepository.findNameById(userId);
    }

    public List<Member> getAllUsers() {
        return memberRepository.findAllUsers();
    }

    public Role getRoleByEmail(String email) {
        Role role = memberRepository.findRoleByEmail(email);
        return role;
    }

    public Member addMember(MemberRequest memberRequest) {
        return memberRepository.addMember(memberRequest);
    }

    public Member findByEmail(String userEmail) {
        return memberRepository.findByEmail(userEmail);
    }
}
