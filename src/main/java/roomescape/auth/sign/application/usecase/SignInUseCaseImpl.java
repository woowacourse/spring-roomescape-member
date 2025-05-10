package roomescape.auth.sign.application.usecase;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.jwt.domain.Jwt;
import roomescape.auth.jwt.domain.TokenType;
import roomescape.auth.jwt.manager.JwtManager;
import roomescape.auth.sign.application.dto.SignInServiceRequest;
import roomescape.auth.sign.exception.InvalidSignInException;
import roomescape.auth.sign.password.PasswordEncoder;
import roomescape.user.application.service.UserQueryService;
import roomescape.user.domain.User;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignInUseCaseImpl implements SignInUseCase {

    private final UserQueryService userQueryService;
    private final PasswordEncoder passwordEncoder;
    private final JwtManager jwtManager;

    @Override
    public void execute(final SignInServiceRequest request,
                        final HttpServletResponse response) {
        final User user = userQueryService.getByEmail(request.email());
        final String savedEncodedPassword = user.getPassword();
        final String inputEncodedPassword = passwordEncoder.execute(request.password());

        final Claims claims = buildClaims(user);

        validatePassword(savedEncodedPassword, inputEncodedPassword);
        setAccessToken(response, jwtManager.generate(claims, TokenType.ACCESS));
    }

    private static void validatePassword(final String savedEncodedPassword,
                                         final String inputEncodedPassword) {
        if (savedEncodedPassword.equals(inputEncodedPassword)) {
            return;
        }
        throw new InvalidSignInException("passwords do not match");
    }

    private Claims buildClaims(final User user) {
        return Jwts.claims()
                .add(User.Fields.id, user.getId().getValue())
                .add(User.Fields.name, user.getName())
                .add(User.Fields.role, user.getRole().name())
                .build();
    }

    // TODO 쿠키 서비스로 구분
    private void setAccessToken(final HttpServletResponse response, final Jwt accessToken) {
        response.addCookie(buildCookie(accessToken));
    }

    private Cookie buildCookie(final Jwt accessToken) {
        final Cookie cookie = new Cookie(TokenType.ACCESS.getDescription(), accessToken.getValue());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(TokenType.ACCESS.getPeriodInSeconds());
        return cookie;
    }
}
