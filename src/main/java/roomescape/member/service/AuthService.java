package roomescape.member.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.handler.exception.CustomBadRequest;
import roomescape.handler.exception.CustomException;
import roomescape.handler.exception.CustomUnauthorized;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberToken;
import roomescape.member.dto.LogInRequest;
import java.util.Date;
import java.util.List;

@Service
public class AuthService {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private Long durationInMilliseconds;

    private final MemberDao memberDao;

    public AuthService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberToken createToken(LogInRequest logInRequest) {
        Claims claims = Jwts.claims().setSubject("user");
        claims.put("email", logInRequest.email());
        validateLogin(logInRequest.email(), logInRequest.password());
        Date now = new Date();
        String token = Jwts.builder()
                .setSubject("RoomEscapeToken")
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + durationInMilliseconds))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return new MemberToken(token);
    }

    private void validateLogin(String principal, String credential) {
        Member member = findByEmail(principal);
        if (!member.hasCredential(credential)) {
            throw new CustomException(CustomUnauthorized.INCORRECT_PASSWORD);
        }
    }

    public Member findByEmail(String email) {
        return memberDao.findMemberByEmail(email)
                .orElseThrow(() -> new CustomException(CustomBadRequest.NOT_MEMBER));
    }

    public String parseEmail(Cookie cookie) {
        if (cookie.getName().equals("token")) {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(cookie.getValue())
                    .getBody()
                    .get("email", String.class);
        }
        throw new CustomException(CustomUnauthorized.NO_LOGIN_TOKEN);
    }

    public void checkAdmin(Cookie cookie) {
        Member member = findByEmail(parseEmail(cookie));
        if (!member.isAdmin()) {
            throw new CustomException(CustomUnauthorized.NOT_AUTHORIZED);
        }
    }

    public Member findById(Long id) {
        return memberDao.findMemberById(id)
                .orElseThrow(() -> new CustomException(CustomBadRequest.NOT_MEMBER));
    }

    public List<Member> findAll() {
        return memberDao.findAllMembers();
    }
}
