package roomescape.common.exceptionHandler;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.common.exceptionHandler.dto.ExceptionResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class GlobalExceptionHandlerTest {

    @TestConfiguration
    static class TestControllerConfig {
        @RestController
        static class TestController {
            @GetMapping("/illegalArgumentException")
            public void illegalArgumentException() {
                throw new IllegalArgumentException("IllegalArgumentException 예외 테스트");
            }

            @GetMapping("/nullPointerException")
            public void nullPointerException() {
                throw new NullPointerException("NullPointerException 예외 테스트");
            }

            @GetMapping("/httpMessageNotReadableException")
            public void httpMessageNotReadableException() {
                throw new HttpMessageNotReadableException("HttpMessageNotReadableException 예외 테스트");
            }

            @GetMapping("/httpMessageNotReadableException2")
            public void httpMessageNotReadableException2() {
                try {
                    throw new IllegalArgumentException("IllegalArgumentException 감싼 예외 테스트");
                } catch (IllegalArgumentException e) {
                    throw new HttpMessageNotReadableException(e.getMessage(), e);
                }
            }
        }
    }

    @Test
    @DisplayName("IllegalArgumentException 처리 테스트")
    void IllegalArgumentException_Handler_Test() {
        ExceptionResponse expected = new ExceptionResponse(400, "[ERROR] IllegalArgumentException 예외 테스트",
                "/illegalArgumentException");

        Response response = RestAssured.given().log().all()
                .when().get("/illegalArgumentException")
                .then().log().all()
                .statusCode(400)
                .extract()
                .response();

        ExceptionResponse actual = response.as(ExceptionResponse.class);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("NullPointerException 처리 테스트")
    void NullPointerException_Handler_Test() {
        ExceptionResponse expected = new ExceptionResponse(500, "[ERROR] 서버의 오류입니다. 관리자에게 문의해주세요.",
                "/nullPointerException");

        Response response = RestAssured.given().log().all()
                .when().get("/nullPointerException")
                .then().log().all()
                .statusCode(500)
                .extract()
                .response();

        ExceptionResponse actual = response.as(ExceptionResponse.class);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("HttpMessageNotReadableException 처리 테스트")
    void HttpMessageNotReadableException_Handler_Test() {
        ExceptionResponse expected = new ExceptionResponse(400, "[ERROR] 요청 입력이 잘못되었습니다.",
                "/httpMessageNotReadableException");

        Response response = RestAssured.given().log().all()
                .when().get("/httpMessageNotReadableException")
                .then().log().all()
                .statusCode(400)
                .extract()
                .response();

        ExceptionResponse actual = response.as(ExceptionResponse.class);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("IllegalArgumentException 감싼 HttpMessageNotReadableException 처리 테스트")
    void IllegalArgumentException_and_HttpMessageNotReadableException_Handler_Test() {
        ExceptionResponse expected = new ExceptionResponse(400, "[ERROR] IllegalArgumentException 감싼 예외 테스트",
                "/httpMessageNotReadableException2");

        Response response = RestAssured.given().log().all()
                .when().get("/httpMessageNotReadableException2")
                .then().log().all()
                .statusCode(400)
                .extract()
                .response();

        ExceptionResponse actual = response.as(ExceptionResponse.class);
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}