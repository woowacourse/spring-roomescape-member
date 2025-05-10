package roomescape.business.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
        final User user = findUserByPrincipalAndCredentials(loginRequest.email(), loginRequest.password());

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("name", user.getName())
                .claim("role", user.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    private User findUserByPrincipalAndCredentials(
            final String principal,
            final String credentials
    ) {
        final User user = userDao.findByPrincipal(principal)
                .orElseThrow(InvalidCredentialsException::new);

        if (!user.getPassword().equals(credentials)) {
            throw new InvalidCredentialsException();
        }

        return user;
    }

    public LoginCheckResponse checkLoginByToken(final String token) {
        try {
            final Long userId = Long.valueOf(Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody().getSubject());

            return new LoginCheckResponse(userId);
        } catch (JwtException e) {
            throw new InvalidCredentialsException();
        }
    }
}
