package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.TokenInfo;
import roomescape.infrastructure.JwtTokenProvider;

@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class AuthControllerTest {

    @Autowired
    JwtTokenProvider tokenProvider;

    @Nested
    class SuccessTest {

        @DisplayName("이메일과 비밀번호가 일치하면 토큰을 발급한다")
        @Test
        void loginTest() {
            // given
            String email = "jeffrey@gmail.com";
            String password = "1234!@#$";

            // when
            String jwtToken = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(Map.of("email", email, "password", password))
                    .when()
                    .post("/login")
                    .getCookie("token");

            // then
            TokenInfo tokenInfo = tokenProvider.getTokenInfo(jwtToken);
            assertThat(tokenInfo.id()).isEqualTo(1);
            assertThat(tokenInfo.name()).isEqualTo("제프리");
            assertThat(tokenInfo.role()).isEqualTo("USER");
        }

        @DisplayName("토큰에서 이름을 추출한다")
        @Test
        void checkInfoTest() {
            // given
            String email = "jeffrey@gmail.com";
            String password = "1234!@#$";

            String jwtToken = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(Map.of("email", email, "password", password))
                    .when()
                    .post("/login")
                    .getCookie("token");

            // when // then
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .cookie("token", jwtToken)
                    .when()
                    .get("/login/check")
                    .then()
                    .statusCode(200)
                    .body("name", equalTo("제프리"));
        }


        @DisplayName("로그아웃시 토큰을 삭제한다")
        @Test
        void logoutTest() {
            // when  // then
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .when()
                    .post("/logout")
                    .then()
                    .statusCode(200)
                    .header(HttpHeaders.SET_COOKIE, containsString("token="))
                    .header(HttpHeaders.SET_COOKIE, containsString("Max-Age=0"))
                    .header(HttpHeaders.SET_COOKIE, containsString("HttpOnly"))
                    .header(HttpHeaders.SET_COOKIE, containsString("Path=/"));
        }
    }

    @Nested
    class FailureTest {

        @DisplayName("이메일과 비밀번호가 일치하지 않으면 401 에러를 반환한다.")
        @Test
        void loginFailureWithEmailIncorrectTest() {
            // given
            String email = "wilson@gmail.com";
            String password = "1234!@#$";

            // when & then
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(Map.of("email", email, "password", password))
                    .when().post("/login")
                    .then().log().all()
                    .statusCode(401);
        }

        @DisplayName("이메일과 비밀번호가 일치하지 않으면 401 에러를 반환한다.")
        @Test
        void loginFailureWithPasswordIncorrectTest() {
            // given
            String email = "jeffrey@gmail.com";
            String password = "1234!";

            // when & then
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(Map.of("email", email, "password", password))
                    .when().post("/login")
                    .then().log().all()
                    .statusCode(401);
        }
    }
}
