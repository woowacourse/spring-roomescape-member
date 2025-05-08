package roomescape.application.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;

@Service
public class LoginService {

    private final MemberDao memberDao;

    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public LoginService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public String createToken(String email) {
        Member member = memberDao.findByEmail(email);

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }
}
