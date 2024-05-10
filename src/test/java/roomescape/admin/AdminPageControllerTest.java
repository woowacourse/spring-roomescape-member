package roomescape.admin;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.provider.JwtTokenProvider;
import roomescape.member.domain.Member;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdminPageControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String adminToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        adminToken = jwtTokenProvider.createToken(
                new Member(1L, "한태웅", "taewoong@example.com", "123", "ADMIN"));
    }

    private void testAdminPage(String path) {
        RestAssured.given()
                .cookie("token", adminToken)
                .log()
                .all()
                .when()
                .get("/admin" + path)
                .then()
                .log()
                .all()
                .statusCode(200);
    }

    @Test
    void admin() {
        testAdminPage("");
    }

    @Test
    void reservation() {
        testAdminPage("/reservation");
    }

    @Test
    void time() {
        testAdminPage("/time");
    }

    @Test
    void theme() {
        testAdminPage("/theme");
    }
}
