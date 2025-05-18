package roomescape.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.config.JwtTokenProvider;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.exception.AuthenticationException;

@Service
public class LoginService {

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(LoginRequest request) {
        Member member = memberDao.findByEmail(request.email())
            .orElseThrow((() -> new AuthenticationException("존재하지 않는 이메일입니다.")));
        if (member.isPasswordNotEqual(request.password())) {
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
        }
        return jwtTokenProvider.get(member);
    }

    public Member getLoginMemberByToken(String token) {
        Long memberId = jwtTokenProvider.getMemberIdFromToken(token);
        return memberDao.findById(memberId)
            .orElseThrow(() -> new AuthenticationException("존재하지 않는 id 입니다"));
    }
}
