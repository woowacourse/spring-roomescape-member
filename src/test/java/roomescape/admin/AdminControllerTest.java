package roomescape.admin;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdminControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void admin() {
        RestAssured.given()
                   .log()
                   .all()
                   .when()
                   .get("/admin")
                   .then()
                   .log()
                   .all()
                   .statusCode(200);
    }

    @Test
    void reservation() {
        RestAssured.given()
                   .log()
                   .all()
                   .when()
                   .get("/admin/reservation")
                   .then()
                   .log()
                   .all()
                   .statusCode(200);
    }

    @Test
    void time() {
        RestAssured.given()
                   .log()
                   .all()
                   .when()
                   .get("/admin/time")
                   .then()
                   .log()
                   .all()
                   .statusCode(200);
    }
}
