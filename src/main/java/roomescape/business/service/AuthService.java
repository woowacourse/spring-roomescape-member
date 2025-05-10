package roomescape.business.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.business.domain.LoginUser;
import roomescape.business.domain.User;
import roomescape.exception.InvalidCredentialsException;
import roomescape.persistence.dao.UserDao;
import roomescape.presentation.dto.LoginCheckResponse;
import roomescape.presentation.dto.LoginRequest;

@Service
public class AuthService {

    @Value("${jwt.secret}")
    private String secretKey;

    private final UserDao userDao;

    public AuthService(final UserDao userDao) {
        this.userDao = userDao;
    }

    public String createToken(final LoginRequest loginRequest) {
        final LoginUser loginUser = findLoginUserByPrincipalAndCredentials(loginRequest.email(), loginRequest.password());

        return Jwts.builder()
                .setSubject(loginUser.id().toString())
                .claim("name", loginUser.name())
                .claim("role", loginUser.role())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public LoginUser findLoginUserByPrincipalAndCredentials(
            final String principal,
            final String credentials
    ) {
        final User user = userDao.findByPrincipal(principal)
                .orElseThrow(InvalidCredentialsException::new);

        if (!user.getPassword().equals(credentials)) {
            throw new InvalidCredentialsException();
        }

        return LoginUser.from(user);
    }

    public LoginCheckResponse checkLoginByToken(final String token) {
        try {
            final String name = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("name", String.class);

            return new LoginCheckResponse(name);
        } catch (JwtException e) {
            throw new InvalidCredentialsException();
        }
    }
}
