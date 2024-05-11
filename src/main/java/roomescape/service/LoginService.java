package roomescape.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.auth.JwtTokenProvider;
import roomescape.auth.LoginFailException;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.dto.LoginRequest;
import roomescape.dto.LoginResponse;

@Service
public class LoginService {

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(LoginRequest loginRequest) {
        if (isExistMember(loginRequest)) {
            return jwtTokenProvider.createToken(loginRequest.getEmail());
        }
        throw new LoginFailException("회원가입 된 멤버가 아닙니다.");
    }

    private boolean isExistMember(LoginRequest loginRequest) {
        return memberDao.isExist(loginRequest.getEmail());
    }

    public LoginResponse tokenLogin(String token) {
        String memberEmail = jwtTokenProvider.findMemberEmailByToken(token);
        Member member = findMemberByEmail(memberEmail);
        return new LoginResponse(member.getName());
    }

    public Member findMemberByEmail(String memberEmail) {
        return memberDao.findMemberByEmail(memberEmail);
    }

    public Member findLoginMember(Cookie[] cookies) {
        String token = jwtTokenProvider.findTokenByCookie(cookies);

        String memberEmail = jwtTokenProvider.findMemberEmailByToken(token);
        return memberDao.findMemberByEmail(memberEmail);
    }
}
