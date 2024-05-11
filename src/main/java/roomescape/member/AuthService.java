package roomescape.member;


import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberDao;
import roomescape.member.request.LoginRequest;
import roomescape.member.response.LoginResponse;
import roomescape.member.response.MemberResponse;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public boolean checkInvalidLogin(String email, String password) {
        return !memberDao.isMember(email, password);
    }

    public Member findMemberByEmail(String email) {
        return memberDao.findMemberByEmail(email);
    }

    public LoginResponse createToken(LoginRequest loginRequest) {
        if (checkInvalidLogin(loginRequest.email(), loginRequest.password())) {
            throw new IllegalArgumentException("login failed");
        }
        Member member = findMemberByEmail(loginRequest.email());
        String accessToken = jwtTokenProvider.createToken(member);

        return new LoginResponse(accessToken);
    }

    public MemberResponse findMemberNameByToken(String token) {
        Long memberId = Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=".getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());
        return new MemberResponse(memberDao.findNameById(memberId));
    }
}
