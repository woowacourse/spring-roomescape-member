package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoginControllerTest {

    @Test
    @DisplayName("/login POST 요청을 통해 로그인에 성공하면 토큰을 발급받는다")
    void login_token() {
        Map<String, Object> params = new HashMap<>();
        params.put("email", "admin@gmail.com");
        params.put("password", "1234");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .header("Set-Cookie", notNullValue());
    }

    @Test
    @DisplayName("/login POST 요청시 존재하지 않는 회원일 시 401을 응답한다")
    void login_member_not_exist() {
        Map<String, Object> params = new HashMap<>();
        params.put("email", "may@gmail.com");
        params.put("password", "1234");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(401);
    }
}
