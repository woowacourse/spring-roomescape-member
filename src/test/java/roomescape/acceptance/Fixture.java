package roomescape.acceptance;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import static roomescape.acceptance.PreInsertedData.preInsertedAdmin;
import static roomescape.acceptance.PreInsertedData.preInsertedCustomer;

public class Fixture {

    static final String secretKey = "testTestTestTestTestTestTestTestTestTestTest";

    public static final String adminToken = Jwts.builder()
            .subject(preInsertedAdmin.getId().toString())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();

    public static final String customerToken = Jwts.builder()
            .subject(preInsertedCustomer.getId().toString())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();
}
