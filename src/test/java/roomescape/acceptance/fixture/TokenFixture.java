package roomescape.acceptance.fixture;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestComponent;

import static roomescape.acceptance.fixture.PreInsertedDataFixture.PRE_INSERTED_ADMIN;
import static roomescape.acceptance.fixture.PreInsertedDataFixture.PRE_INSERTED_CUSTOMER_1;

@TestComponent
public class TokenFixture {

    public final String secretKey;
    public final String adminToken;
    public final String customerToken;

    public TokenFixture(@Value("${jwt.secret-key}") String secretKey) {
        this.secretKey = secretKey;

        this.adminToken = Jwts.builder()
                .subject(PRE_INSERTED_ADMIN.getId().toString())
                .claim("role", PRE_INSERTED_ADMIN.getRole().name())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();

        this.customerToken = Jwts.builder()
                .subject(PRE_INSERTED_CUSTOMER_1.getId().toString())
                .claim("role", PRE_INSERTED_CUSTOMER_1.getRole().name())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
}
