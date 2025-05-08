package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class RoomescapeApplicationTest {
        @Test
        void contextLoads() {
        }

        @DisplayName("로그인 요청시 set-cookie로 토큰을 받을 수 있다.")
        @Test
        void setCookieHeaderWhenLogin() {
                Map<String, String> params = Map.of(
                        "email", "example@test.com",
                        "password", "1234"
                );

                ExtractableResponse<Response> response = RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .body(params)
                        .when()
                        .post("/login")
                        .then().log().all()
                        .statusCode(200)
                        .extract();

                assertThat(response.cookie("token")).isNotEmpty();
        }
}
