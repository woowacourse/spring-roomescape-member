package roomescape.api;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FrontPageApiTest {

    @LocalServerPort
    int port;

    @Test
    void 인기_테마_페이지_이동() {
        given().log().all()
                .port(port)
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 예약_페이지_이동() {
        given().log().all()
                .port(port)
                .when().get("/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 로그인_페이지_이동() {
        given().log().all()
                .port(port)
                .when().get("/login")
                .then().log().all()
                .statusCode(200);
    }
}
