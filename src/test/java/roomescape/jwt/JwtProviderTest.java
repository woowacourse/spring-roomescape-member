package roomescape.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
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
        JwtRequest jwtRequest = new JwtRequest("test", "test@email.com", MemberRoleType.ADMIN, issueDate);

        //when
        String actual = jwtProvider.generateToken(jwtRequest);

        //then
        String expected = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGVtYWlsLmNvbSIsIm5hbWUiOiJ0ZXN0IiwiZW1haWwiOiJ0ZXN0QGVtYWlsLmNvbSIsInJvbGUiOiJBRE1JTiIsImlzcyI6InJvb21lc2NhcGUiLCJpYXQiOjAsImV4cCI6ODY0MDB9.y1iN4Rm18gUCX3We1vIF0V37M5K3RNUHhDorC6JBkeA";
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
        JwtRequest jwtRequest = new JwtRequest("test", "test@email.com", MemberRoleType.ADMIN, issueDate);
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(SECRET_KEY));
        String token = Jwts.builder()
                .subject(jwtRequest.email())
                .claim("name", jwtRequest.name())
                .claim("email", jwtRequest.email())
                .claim("role", jwtRequest.memberRoleType())
                .issuer(ISSUER)
                .issuedAt(jwtRequest.issuedAt())
                .expiration(new Date(jwtRequest.issuedAt().getTime() + 100000))
                .signWith(secretKey, SIG.HS256)
                .compact();

        //when
        JwtRequest actual = jwtProvider.verifyToken(token);

        //then
        assertAll(
                () -> assertThat(actual.email()).isEqualTo(jwtRequest.email()),
                () -> assertThat(actual.memberRoleType()).isEqualTo(actual.memberRoleType()),
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
        JwtRequest jwtRequest = new JwtRequest("test", "test@email.com", MemberRoleType.ADMIN, issueDate);
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(SECRET_KEY));
        String token = Jwts.builder()
                .subject(jwtRequest.email())
                .claim("name", jwtRequest.name())
                .claim("email", jwtRequest.email())
                .claim("role", jwtRequest.memberRoleType())
                .issuer(ISSUER)
                .issuedAt(jwtRequest.issuedAt())
                .expiration(new Date(jwtRequest.issuedAt().getTime() - 10000))
                .signWith(secretKey, SIG.HS256)
                .compact();

        //when //then
        assertThatThrownBy(() -> jwtProvider.verifyToken(token))
                .isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    @DisplayName("secretKey가 일치하지 않는 경우, 예외를 던진다")
    void throwExceptionWhenWrongSecretKey() {
        //given
        Date issueDate = new Date();
        JwtRequest jwtRequest = new JwtRequest("test", "test@email.com", MemberRoleType.ADMIN, issueDate);
        SecretKey wrongSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(SECRET_KEY + "A"));
        String token = Jwts.builder()
                .subject(jwtRequest.email())
                .claim("name", jwtRequest.name())
                .claim("email", jwtRequest.email())
                .claim("role", jwtRequest.memberRoleType())
                .issuer(ISSUER)
                .issuedAt(jwtRequest.issuedAt())
                .expiration(new Date(jwtRequest.issuedAt().getTime() - 10000))
                .signWith(wrongSecretKey, SIG.HS256)
                .compact();

        //when //then
        assertThatThrownBy(() -> jwtProvider.verifyToken(token))
                .isInstanceOf(SignatureException.class);
    }
}
