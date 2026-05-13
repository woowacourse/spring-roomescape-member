package roomescape;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
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

}
