package roomescape.view.integration;

import io.restassured.RestAssured;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.member.dao.MemberJdbcDao;
import roomescape.member.domain.Member;
import roomescape.member.security.crypto.JwtTokenProvider;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/schema-test.sql", "/data-test.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class AdminIntegrationTest {

    @LocalServerPort
    private int port;
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;
    @Autowired
    private MemberJdbcDao memberJdbcDao;

    private String token;

    @BeforeEach
    void init() {
        RestAssured.port = port;
        Member member = new Member(1, "어드민", "admin@email.com", "pass", "ADMIN");
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(secretKey, validityInMilliseconds);
        token = jwtTokenProvider.createToken(member, new Date());
    }

    @Test
    @DisplayName("관리자 메인 페이지가 잘 접속된다.")
    void adminMainPageLoad() {

        RestAssured.given()
                .cookie("token", token)
                .when()
                .get("/admin")
                .then()
                .statusCode(200);
    }

}
