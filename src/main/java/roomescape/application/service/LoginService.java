package roomescape.application.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.application.dto.LoginRequest;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;

@Service
public class LoginService {

    private final MemberDao memberDao;

    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public LoginService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public String createTokenForAuthenticatedMember(LoginRequest request) {
        Optional<Member> member = memberDao.findByEmail(request.email());

        if (member.isEmpty()) {
            throw new IllegalArgumentException("로그인을 실패했습니다. 정보를 다시 확인해 주세요.");
        }

        return Jwts.builder()
                .setSubject(member.get().getId().toString())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }
}
