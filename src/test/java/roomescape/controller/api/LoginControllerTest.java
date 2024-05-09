package roomescape.controller.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import roomescape.controller.dto.LoginRequest;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class LoginControllerTest {

    @DisplayName("성공: 로그인 성공")
    @Test
    void login_Success() {
        LoginRequest request = new LoginRequest("a@a.com", "123a!");
        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/login")
            .then().log().all()
            .statusCode(200);
    }

    @DisplayName("실패: 잘못된 이메일 혹은 잘못된 비밀번호")
    @ParameterizedTest
    @CsvSource(value = {"a@a.com,1234", "b@a.com,123!", "b@a.com,1234"})
    void login_WrongEmail_Or_WrongPassword(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);
        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/login")
            .then().log().all()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}
