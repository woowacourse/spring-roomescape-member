package roomescape.handle;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GlobalExceptionHandlerTest {

    @Test
    @DisplayName("요청 경로가 존재하지 않으면 예외를 잡아 처리한다.")
    void handleNoResourceFoundException() {
        RestAssured.given().log().all()
                .when().get("/invalid-path")
                .then().log().all()
                .statusCode(404)
                .body(equalTo("입력된 경로와 일치하는 페이지가 없습니다."));
    }

    @Test
    @DisplayName("요청 HTTP 메서드를 지원하지 않으면 예외를 잡아 처리한다.")
    void handleHttpRequestMethodNotSupportedException() {
        RestAssured.given().log().all()
                .when().patch("/reservations")
                .then().log().all()
                .statusCode(405)
                .body(equalTo("요청된 HTTP 메서드를 지원하지 않습니다."));
    }
}
