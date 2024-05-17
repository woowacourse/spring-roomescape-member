package roomescape.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.auth.JwtTokenProvider;
import roomescape.exceptions.LoginFailException;
import roomescape.repository.MemberRepository;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.LoginResponse;

@Service
public class LoginService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(LoginRequest loginRequest) {
        if (isExistMember(loginRequest)) {
            Member member = memberRepository.findMemberByEmail(loginRequest.getEmail());
            return jwtTokenProvider.createToken(member.getId(), member.getRole());
        }
        throw new LoginFailException("회원가입 된 멤버가 아닙니다.");
    }

    private boolean isExistMember(LoginRequest loginRequest) {
        return memberRepository.isExist(loginRequest.getEmail());
    }

    public LoginResponse checkLoginMember(String token) {
        String memberId = jwtTokenProvider.findMemberIdByToken(token);
        Member member = findMemberById(memberId);
        return new LoginResponse(member.getName());
    }

    public Member findMemberById(String memberId) {
        return memberRepository.findMemberById(Long.parseLong(memberId));
    }

    public Member findLoginMember(Cookie[] cookies) {
        String token = jwtTokenProvider.findTokenByCookie(cookies);

        String memberId = jwtTokenProvider.findMemberIdByToken(token);
        return memberRepository.findMemberById(Long.parseLong(memberId));
    }

    public boolean isAdminMember(Cookie[] cookies) {
        String token = jwtTokenProvider.findTokenByCookie(cookies);
        String roleName = jwtTokenProvider.findRole(token);
        return Role.findRole(roleName).equals(Role.ADMIN);
    }
}
