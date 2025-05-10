package roomescape;


import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MemberControllerTest {


    @Test
    @DisplayName("로그인 시 헤더에 토큰을 반환한다.")
    void loginTest() {
        Map<String, String> params = new HashMap<>();
        params.put("email", "abc");
        params.put("password", "def");
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("auth/login")
                .then().log().all()
                .statusCode(200)
                .extract().response();
        String setCookie = response.getHeader("Set-Cookie");

        Assertions.assertThat(setCookie).isNotNull();
    }

    @Test
    @DisplayName("로그인 체크 시 인증하고 이름을 반환한다.")
    void loginCheckTest() {
        Map<String, String> params = new HashMap<>();
        params.put("email", "abc");
        params.put("password", "def");

        Response loginResp = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/auth/login")
                .then().log().all()
                .statusCode(200)
                .extract().response();

        String token = loginResp.getCookie("token");
        Assertions.assertThat(token).isNotNull();

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/auth/login/check")
                .then().log().all()
                .statusCode(200)
                .body("name", is("vector"));
    }


}
