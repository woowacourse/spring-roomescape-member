package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.MemberRequest;
import roomescape.dto.MemberResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LoginControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("로그인이 정상적으로 되었는지 확인한다.")
    void login() {
        // given
        MemberRequest memberRequest = new MemberRequest("password1", "member1@email.com");

        // when
        Cookies cookies = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(memberRequest)
                .when().post("/login")
                .then().log().all()
                .statusCode(200).extract()
                .response().getDetailedCookies();

        // then
        assertThat(cookies).isNotEmpty();
    }

    @Test
    @DisplayName("로그인 체크가 정상적으로 되었는지 확인한다.")
    void loginCheck() {
        // given
        MemberRequest memberRequest = new MemberRequest("password1", "member1@email.com");

        Cookies cookies = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(memberRequest)
                .when().post("/login")
                .then().log().all().extract()
                .response().getDetailedCookies();

        // when
        MemberResponse memberResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .extract().response().as(MemberResponse.class);

        // then
        assertThat(memberResponse.name()).isEqualTo("사용자1");
    }

    @Test
    @DisplayName("로그아웃이 정상적으로 되었는지 확인한다.")
    void logout() {
        // given
        MemberRequest memberRequest = new MemberRequest("password1", "member1@email.com");

        Cookies cookiesAfterLogin = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(memberRequest)
                .when().post("/login")
                .then().log().all().extract()
                .response().getDetailedCookies();

        // when
        Cookie cookiesAfterLogout = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookies(cookiesAfterLogin)
                .when().post("/logout")
                .then().log().all()
                .statusCode(200).extract()
                .response().getDetailedCookie("token");

        // then
        assertThat(cookiesAfterLogout.getValue()).isEmpty();
    }
}
