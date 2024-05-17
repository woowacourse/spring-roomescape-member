package roomescape.controller.auth;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import roomescape.controller.ControllerTest;
import roomescape.service.auth.dto.LoginRequest;
import roomescape.service.auth.dto.SignUpRequest;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:truncate-with-admin-and-guest.sql"})
class AuthControllerTest extends ControllerTest {
    private String token;

    @DisplayName("로그인 성공 테스트")
    @Test
    void login() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("guest123", "guest@email.com"))
                .when().post("/login")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.OK.value());
    }

    @DisplayName("로그아웃 성공 테스트")
    @TestFactory
    Stream<DynamicTest> logout() {
        return Stream.of(
                DynamicTest.dynamicTest("로그인을 한다.", () -> {
                    token = RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(new LoginRequest("guest123", "guest@email.com"))
                            .when().post("/login")
                            .then().log().all().extract().cookie("token");
                }),
                DynamicTest.dynamicTest("로그아웃을 한다.", () -> {
                    RestAssured.given().log().all()
                            .cookie("token", token)
                            .contentType(ContentType.JSON)
                            .when().post("/logout")
                            .then().log().all()
                            .assertThat().statusCode(HttpStatus.OK.value());
                })
        );
    }

    @DisplayName("인증 조회 성공 테스트")
    @TestFactory
    Stream<DynamicTest> check() {
        return Stream.of(
                DynamicTest.dynamicTest("로그인을 한다.", () -> {
                    token = RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(new LoginRequest("guest123", "guest@email.com"))
                            .when().post("/login")
                            .then().log().all().extract().cookie("token");
                }),
                DynamicTest.dynamicTest("인증을 조회한다.", () -> {
                    RestAssured.given().log().all()
                            .cookie("token", token)
                            .when().get("/login/check")
                            .then().log().all()
                            .assertThat().statusCode(HttpStatus.OK.value()).body("name", is("guest"));
                })
        );
    }

    @DisplayName("회원가입 성공 테스트")
    @Test
    void signUp() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new SignUpRequest("lini", "lini@email.com", "lini123"))
                .when().post("/signup")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.CREATED.value());
    }
}
