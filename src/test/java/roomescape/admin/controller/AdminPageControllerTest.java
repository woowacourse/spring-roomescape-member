package roomescape.admin.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.base.BaseTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdminPageControllerTest extends BaseTest {

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
                .statusCode(HttpStatus.OK.value());
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
