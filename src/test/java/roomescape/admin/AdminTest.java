package roomescape.admin;

import java.util.HashMap;
import java.util.Map;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static roomescape.fixture.MemberFixture.adminFixture;
import static roomescape.fixture.MemberFixture.memberFixture;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AdminTest {

    private String token;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        adminLogIn();
    }

    void adminLogIn() {
        Map<String, String> params = new HashMap<>();
        params.put("email", adminFixture.getEmail());
        params.put("password", adminFixture.getPassword());

        String cookie = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie");
        token = cookie.substring("token=".length(), cookie.indexOf(';'));
    }

    @DisplayName("로그인하지 않고 어드민 페이지에 접근하면, 로그인 페이지로 리다이렉트 된다")
    @Test
    void accessToAdminPageWhenNotLogIn() {
        RestAssured.given().log().all()
                .redirects().follow(false)
                .when().get("/admin")
                .then().log().all()
                .statusCode(302)
                .header("Location", StringContains.containsString("/login"));
    }

    @DisplayName("일반 멤버 로그인 시 어드민 페이지에 접근할 수 없다")
    @Test
    void accessToAdminPageWhenUserLogIn() {
        Map<String, String> params = new HashMap<>();
        params.put("email", memberFixture.getEmail());
        params.put("password", memberFixture.getPassword());

        String cookie = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie");
        String memberToken = cookie.substring("token=".length(), cookie.indexOf(';'));

        RestAssured.given().log().all()
                .cookie("token", memberToken)
                .when().get("/admin")
                .then().log().all()
                .statusCode(401);
    }

    @DisplayName("어드민 메인 페이지 테스트")
    @Test
    void adminMainPage() {
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("예약 어드민 페이지 테스트")
    @Test
    void readReservations() {
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("시간 추가 페이지 테스트")
    @Test
    void timePage() {
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("테마 추가 페이지 테스트")
    @Test
    void themePage() {
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }
}
