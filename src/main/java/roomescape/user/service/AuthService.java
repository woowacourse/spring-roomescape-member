package roomescape.user.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.user.domain.User;
import roomescape.user.repository.UserDao;
import roomescape.user.service.dto.LoginInfo;
import roomescape.user.service.dto.TokenResponse;
import roomescape.user.service.dto.UserInfo;

@Service
public class AuthService {

    private static final SignatureAlgorithm SIGN_ALGORITHM = SignatureAlgorithm.HS256;
    @Value("${security.jwt.token.secret_key}")
    private String SECRET_KEY;
    @Value("${security.jwt.token.expriation_term}")
    private long EXPIRATION_TERM;

    private final UserDao userDao;

    public AuthService(final UserDao userDao) {
        this.userDao = userDao;
    }

    public TokenResponse tokenLogin(final LoginInfo loginInfo) {
        final User loginUser = userDao.findByEmailAndPassword(loginInfo.email(), loginInfo.password())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일 혹은 비밀번호입니다."));
        final String email = loginUser.getEmail();
        return new TokenResponse(createToken(email));
    }

    public UserInfo getUserInfoByToken(String token) {
        String email = parsePayload(token);
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 정보입니다."));
        return new UserInfo(user);
    }

    private String createToken(String payload) {
        final Date now = new Date();
        final Date expirationDate = new Date(now.getTime() + EXPIRATION_TERM);
        return Jwts.builder()
                .setSubject(payload)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SIGN_ALGORITHM, SECRET_KEY)
                .compact();
    }

    private String parsePayload(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }
}
