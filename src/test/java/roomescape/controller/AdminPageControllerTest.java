package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@Import(TestConfig.class)
public class AdminPageControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("/admin/theme 요청시 테마 관리 페이지를 응답한다.")
    void response_theme_page() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/admin/theme")
                .then()
                .statusCode(200);
    }

}
