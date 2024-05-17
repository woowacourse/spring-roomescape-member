package roomescape.controller.page;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import roomescape.controller.ControllerTest;
import roomescape.service.auth.dto.LoginRequest;

import java.util.stream.Stream;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:truncate-with-admin-and-guest.sql"})
class MemberPageControllerTest extends ControllerTest {
    private String token;

    @DisplayName("사용자 기본 Page 접근 성공 테스트")
    @Test
    void responseMemberMainPage() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.OK.value());
    }

    @DisplayName("사용자 예약 Page 접근 성공 테스트")
    @TestFactory
    Stream<DynamicTest> responseMemberReservationPage() {
        return Stream.of(
                DynamicTest.dynamicTest("로그인을 한다.", () -> {
                    token = RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(new LoginRequest("guest123", "guest@email.com"))
                            .when().post("/login")
                            .then().log().all().extract().cookie("token");
                }),
                DynamicTest.dynamicTest("예약 페이지를 들어간다.", () -> {
                    RestAssured.given().log().all()
                            .cookie("token", token)
                            .when().get("/reservation")
                            .then().log().all()
                            .assertThat().statusCode(HttpStatus.OK.value());
                })
        );
    }

    @DisplayName("사용자 로그인 Page 접근 성공 테스트")
    @Test
    void responseMemberLoginPage() {
        RestAssured.given().log().all()
                .when().get("/login")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.OK.value());
    }

    @DisplayName("사용자 회원가입 Page 접근 성공 테스트")
    @Test
    void responseMemberSignupPage() {
        RestAssured.given().log().all()
                .when().get("/signup")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.OK.value());
    }
}
