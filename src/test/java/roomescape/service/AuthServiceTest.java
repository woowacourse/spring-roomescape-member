package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.dao.MemberDao;
import roomescape.domain.member.Member;
import roomescape.dto.member.LoginRequest;
import roomescape.dto.member.LoginResponse;
import roomescape.exception.AuthorizationException;
import roomescape.fixture.MemberFixtures;
import roomescape.jwt.JwtTokenProvider;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:test_db_clean.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class AuthServiceTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private MemberDao memberDao;
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
        String name = "daon";
        Member member = memberDao.create(MemberFixtures.createUserMember(name, email, "1234"));
        String token = jwtTokenProvider.createToken(email);

        //when
        Member result = authService.findAuthInfo(token);

        //then
        assertAll(
                () -> assertThat(member.getId()).isEqualTo(result.getId()),
                () -> assertThat(member.getName().getValue()).isEqualTo(result.getName().getValue()),
                () -> assertThat(member.getEmail().getValue()).isEqualTo(result.getEmail().getValue()),
                () -> assertThat(member.getPassword().getValue()).isEqualTo(result.getPassword().getValue())
        );
    }

    @Test
    @DisplayName("토큰이 만료되었다면 예외가 발생한다.")
    void findAuthInfoWithExpiredToken() {
        //given
        String expiredToken = generateExpiredToken();

        //when //then
        assertThatThrownBy(() -> authService.findAuthInfo(expiredToken))
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    @DisplayName("인증 정보가 회원 저장소에 없다면 예외가 발생한다.")
    void findAuthInfoWhenNotExistData() {
        //given
        String email = "test@test.com";

        //when //then
        assertThatThrownBy(() -> authService.findAuthInfo(email))
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    @DisplayName("로그인 정보가 존재하지 않으면 예외가 발생한다.")
    void checkLoginInfoWithNotExist() {
        //given
        LoginRequest loginRequest = new LoginRequest("test@test.com", "1111");

        //when //then
        assertThatThrownBy(() -> authService.checkLoginInfo(loginRequest))
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
