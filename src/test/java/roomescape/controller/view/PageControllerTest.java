package roomescape.controller.view;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.MemberRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PageControllerTest {

    private Cookies cookies;

    @LocalServerPort
    int port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("/reservation 페이지의 경우 로그인한 사용자는 접속이 가능하다.")
    void reservationPage_member_401() {
        // given
        loginMember();

        // when  && then
        RestAssured.given().log().all()
                .cookies(cookies)
                .when().get("/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("/reservation 페이지의 경우 로그인하지 않은 사용자는 접근 권한 제한을 받는다.")
    void reservationPage_notLogin_401() {
        RestAssured.given().log().all()
                .when().get("/reservation")
                .then().log().all()
                .statusCode(401);
    }

    void loginMember() {
        MemberRequest memberRequest = new MemberRequest("password1", "member1@email.com");

        cookies = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(memberRequest)
                .when().post("/login")
                .then().log().all()
                .extract()
                .response()
                .getDetailedCookies();
    }
}
