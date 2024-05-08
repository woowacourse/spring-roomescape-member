package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import roomescape.dto.member.LoginRequest;
import roomescape.dto.member.LoginResponse;
import roomescape.exception.AuthorizationException;
import roomescape.jwt.JwtTokenProvider;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AuthServiceTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private AuthService authService;
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Test
    @DisplayName("토큰을 생산하면 email을 기반으로 토큰이 생성된다.")
    void createToken() {
        //given
        String givenEmail = "email@email.com";
        LoginRequest loginRequest = new LoginRequest(givenEmail, "1234");

        //when
        LoginResponse loginResponse = authService.createToken(loginRequest);
        String result = jwtTokenProvider.getPayload(loginResponse.getAccessToken());

        //then
        assertThat(result).isEqualTo(givenEmail);
    }

    @Test
    @DisplayName("토큰을 통해 전송 데이터 반환한다.")
    void findAuthInfo() {
        //given
        String email = "test@test.com";
        String token = jwtTokenProvider.createToken(email);

        //when
        String payload = authService.findPayload(token);

        //then
        assertThat(payload).isEqualTo(email);
    }

    @Test
    @DisplayName("토큰이 만료되었다면 예외가 발생한다.")
    void findAuthInfoWithExpiredToken() {
        //given
        String expiredToken = generateExpiredToken();

        //when //then
        assertThatThrownBy(() -> authService.findPayload(expiredToken))
                .isInstanceOf(AuthorizationException.class);
    }


    private String generateExpiredToken() {
        Claims claims = Jwts.claims().setSubject("test@test.com");
        Date now = new Date();
        Date validity = new Date(now.getTime());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
