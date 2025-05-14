package roomescape.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.MemberRoleType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;


class JwtProviderTest {

    static final String SECRET_KEY = "WW4ya2ppYmRkFG5QSjJBRhjrtew434erworfnsjrefdUYUV5cGrty54u6uterwqe3E1RT1qeXJ0ZWYzNGZkNDVyZmc";
    public static final String ISSUER = "roomescape";
    JwtProvider jwtProvider;

    @BeforeEach
    void setup() {
        this.jwtProvider = new JwtProvider(SECRET_KEY);
    }

    @Test
    @DisplayName("토큰을 생성한다")
    void generateToken() {
        //given
        Date issueDate = new Date(0);
        JwtRequest jwtRequest = new JwtRequest(1, "test", MemberRoleType.ADMIN, issueDate);

        //when
        String actual = jwtProvider.generateToken(jwtRequest);

        //then
        String expected = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6InRlc3QiLCJyb2xlIjoiQURNSU4iLCJpc3MiOiJyb29tZXNjYXBlIiwiaWF0IjowLCJleHAiOjg2NDAwfQ.EAPhQGV6GusOfRhBcszd4hjUNc7oWHLGhDAsxNko2i4";
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isEqualTo(expected)
        );
    }

    @Test
    @DisplayName("토큰 검증 과정에 문제가 없으면 통과한다")
    void verifyToken() {
        //given
        Date issueDate = new Date();
        JwtRequest jwtRequest = new JwtRequest(1, "test", MemberRoleType.ADMIN, issueDate);
        SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder()
                .subject(Long.toString(jwtRequest.id()))
                .claim("name", jwtRequest.name())
                .claim("role", jwtRequest.role())
                .issuer(ISSUER)
                .issuedAt(jwtRequest.issuedAt())
                .expiration(new Date(jwtRequest.issuedAt().getTime() + 100000))
                .signWith(secretKey, SIG.HS256)
                .compact();

        //when
        JwtRequest actual = jwtProvider.verifyToken(token);

        //then
        assertAll(
                () -> assertThat(actual.name()).isEqualTo(jwtRequest.name()),
                () -> assertThat(actual.role()).isEqualTo(jwtRequest.role()),
                () -> assertThat(roundOfMill(actual.issuedAt())).isEqualTo(roundOfMill(jwtRequest.issuedAt()))
        );
    }

    private Date roundOfMill(Date date) {
        return new Date(date.getTime() / 1000 * 1000);
    }

    @Test
    @DisplayName("만료 시간 초과 토큰일 경우, 예외를 던진다")
    void throwExceptionWhenExpiredToken() {
        //given
        Date issueDate = new Date();
        JwtRequest jwtRequest = new JwtRequest(1, "test", MemberRoleType.ADMIN, issueDate);
        SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder()
                .subject(Long.toString(jwtRequest.id()))
                .claim("name", jwtRequest.name())

                .claim("role", jwtRequest.role())
                .issuer(ISSUER)
                .issuedAt(jwtRequest.issuedAt())
                .expiration(new Date(jwtRequest.issuedAt().getTime() - 10000))
                .signWith(secretKey, SIG.HS256)
                .compact();

        //when //then
        assertThatThrownBy(() -> jwtProvider.verifyToken(token))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("secretKey가 일치하지 않는 경우, 예외를 던진다")
    void throwExceptionWhenWrongSecretKey() {
        //given
        Date issueDate = new Date();
        JwtRequest jwtRequest = new JwtRequest(1, "test", MemberRoleType.ADMIN, issueDate);
        SecretKey wrongSecretKey = Keys.hmacShaKeyFor((SECRET_KEY + "A").getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder()
                .subject(Long.toString(jwtRequest.id()))
                .claim("name", jwtRequest.name())

                .claim("role", jwtRequest.role())
                .issuer(ISSUER)
                .issuedAt(jwtRequest.issuedAt())
                .expiration(new Date(jwtRequest.issuedAt().getTime() - 10000))
                .signWith(wrongSecretKey, SIG.HS256)
                .compact();

        //when //then
        assertThatThrownBy(() -> jwtProvider.verifyToken(token))
                .isInstanceOf(Exception.class);
    }
}
