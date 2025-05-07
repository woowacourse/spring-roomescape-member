package roomescape.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.exception.LoginFailedException;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final String secretKey;
    private final Long tokenValidTime;

    public AuthService(
            MemberDao memberDao,
            @Value("${auth.jwt.secret-key}") String secretKey,
            @Value("${auth.jwt.valid-time}") Long tokenValidTime
    ) {
        this.memberDao = memberDao;
        this.secretKey = secretKey;
        this.tokenValidTime = tokenValidTime;
    }

    public String createToken(LoginRequest loginRequest) {
        Member member = memberDao.findByEmail(loginRequest.email())
                .orElseThrow(LoginFailedException::new);
        member.validatePassword(loginRequest.password());

        return Jwts.builder()
                .subject(member.getId().toString())
                .expiration(new Date(new Date().getTime() + tokenValidTime))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}
