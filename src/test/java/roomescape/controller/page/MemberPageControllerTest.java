package roomescape.controller.page;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import roomescape.service.auth.dto.LoginRequest;
import roomescape.service.auth.dto.SignUpRequest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MemberPageControllerTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void init() {
        RestAssured.port = port;
    }

    @DisplayName("사용자 기본 Page 접근 성공 테스트")
    @Test
    void responseMemberMainPage() {
        Response response = RestAssured.given().log().all()
                .when().get("/")
                .then().log().all().extract().response();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("사용자 예약 Page 접근 성공 테스트")
    @Test
    void responseMemberReservationPage() {
        //given
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new SignUpRequest("lini", "lini@email.com", "lini123"))
                .when().post("/signup")
                .then().log().all();

        String token = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("lini123", "lini@email.com"))
                .when().post("/login")
                .then().log().all().extract().cookie("token");

        //when&then
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/reservation")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.OK.value());
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
