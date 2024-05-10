package roomescape.member.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberToken;
import roomescape.member.dto.LogInRequest;
import roomescape.member.dto.MemberInfo;
import roomescape.reservation.handler.exception.CustomBadRequest;
import roomescape.reservation.handler.exception.CustomException;
import java.util.Arrays;
import java.util.Date;

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
        Member member = memberDao.findMemberByEmail(principal)
                .orElseThrow(() -> new CustomException(CustomBadRequest.PAST_TIME_SLOT_RESERVATION));
        if (!member.getPassword().equals(credential)) {
            throw new IllegalArgumentException();
        }
    }

    public MemberInfo getLoginInfo(HttpServletRequest request) {
        String tokenValue = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new CustomException(CustomBadRequest.PAST_TIME_SLOT_RESERVATION));

        String email = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(tokenValue).getBody().get("email", String.class);
        Member member = findByEmail(email);
        return new MemberInfo(member.getName());
    }

    private Member findByEmail(String email) {
        return memberDao.findMemberByEmail(email)
                .orElseThrow(() -> new CustomException(CustomBadRequest.PAST_TIME_SLOT_RESERVATION));
    }
}
