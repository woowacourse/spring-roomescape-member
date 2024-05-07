package roomescape.advice;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GlobalExceptionHandlerTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("IllegalArgumentException가 던져지면 400 에러를 반환한다.")
    @Test
    void handleIllegalArgumentException() {
        RestAssured.given().log().all()
                .when().get("/test/illegalArgumentException")
                .then().log().all()
                .statusCode(400)
                .body("errorMessage", is("예외 메시지"));
    }

    @DisplayName("NullPointerException이 던져지면 400 에러를 반환한다.")
    @Test
    void handleNullPointerException() {
        RestAssured.given().log().all()
                .when().get("/test/nullPointerException")
                .then().log().all()
                .statusCode(400)
                .body("errorMessage", is("인자 중 null 값이 존재합니다."));
    }

    @DisplayName("예상치 못한 에러가 발생하면 500 에러를 반환한다.")
    @Test
    void handleUnexpectedException() {
        RestAssured.given().log().all()
                .when().get("/test/unexpectedException")
                .then().log().all()
                .statusCode(500)
                .body("errorMessage", is("예상치 못한 예외가 발생했습니다. 관리자에게 문의하세요."));
    }
}
