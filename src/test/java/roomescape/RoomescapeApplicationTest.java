package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class RoomescapeApplicationTest {

    @Test
    void contextLoads() {
    }

    @DisplayName("로그인 요청시 set-cookie로 토큰을 받을 수 있다.")
    @Test
    void setCookieHeaderWhenLogin() {
        // signup
        String email = "example@test.com";
        String password = "1234";
        String name = "test";

        Map<String, String> signupParams = Map.of(
                "email", email,
                "password", password,
                "name", name
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(signupParams)
                .when()
                .post("/members")
                .then().log().all()
                .statusCode(201);

        // login
        Map<String, String> loginParams = Map.of(
                "email", email,
                "password", password
        );

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginParams)
                .when()
                .post("/login")
                .then().log().all()
                .statusCode(200)
                .extract();

        assertThat(response.cookie("token")).isNotEmpty();
    }
}
