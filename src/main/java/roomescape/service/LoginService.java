package roomescape.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.dto.LoginRequest;
import roomescape.dto.MemberResponse;
import roomescape.exception.AuthenticationException;

@Service
public class LoginService {

    private final MemberDao userDao;

    private static final String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    public LoginService(MemberDao userDao) {
        this.userDao = userDao;
    }

    public String login(LoginRequest request) {
        Member user = userDao.findByEmail(request.email())
            .orElseThrow((() -> new AuthenticationException("존재하지 않는 이메일입니다.")));
        if(user.isPasswordNotEqual(request.password())) {
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
        }
        return Jwts.builder()
            .claim(Claims.SUBJECT, user.getId().toString())
            .claim("name", user.getName())
            .claim("email", user.getEmail())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();
    }

    public MemberResponse getMemberByToken(String token) {
        Long memberId = Long.valueOf(Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .build()
            .parseSignedClaims(token)
            .getPayload().getSubject());
        Member user = userDao.findById(memberId)
            .orElseThrow(() -> new AuthenticationException("존재하지 않는 id 입니다"));
        return MemberResponse.from(user);
    }
}
