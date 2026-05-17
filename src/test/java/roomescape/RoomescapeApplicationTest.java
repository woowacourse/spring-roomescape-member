package roomescape;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class RoomescapeApplicationTest {

    @Test
    void contextLoads() {
    }

    @Test
    void 존재하지_않는_경로를_요청하면_404를_응답한다() {
        RestAssured.given().log().all()
                .when().get("/not-exist")
                .then().log().all()
                .statusCode(404)
                .body("message", is("존재하지 않는 요청입니다."));
    }

    @ParameterizedTest
    @CsvSource({
            "POST, /themes",
            "POST, /times",
            "PUT, /reservations",
            "POST, /admin/reservations",
            "GET, /admin/themes",
            "GET, /admin/times"
    })
    void 지원하지_않는_HTTP_메서드로_요청하면_405를_응답한다(String method, String path) {
        RestAssured.given().log().all()
                .when().request(method, path)
                .then().log().all()
                .statusCode(405)
                .body("message", is("API 메서드가 잘못되었습니다."));
    }

}
