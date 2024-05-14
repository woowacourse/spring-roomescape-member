package roomescape.controller.api;

import static roomescape.util.Fixture.ADMIN_EMAIL;
import static roomescape.util.Fixture.ADMIN_PASSWORD;
import static roomescape.util.Fixture.MEMBER_EMAIL;
import static roomescape.util.Fixture.MEMBER_PASSWORD;
import static roomescape.util.Fixture.TOMORROW;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
class AdminControllerTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("관리자가 회원을 지정하여 예약을 생성한다.")
    void createReservationByAdmin() {
        final String adminToken = getAdminToken();
        final Map<String, Object> params = new HashMap<>();
        params.put("date", TOMORROW);
        params.put("timeId", 1);
        params.put("themeId", 1);
        params.put("memberId", 1);

        RestAssured.given().log().all()
                .header("Cookie", adminToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("관리자 예약 생성 시, 관리자가 로그인한 경우가 아니면 예외가 발생한다.")
    void createReservationByAdminWithNotAdmin() {
        final String memberToken = getMemberToken();
        final Map<String, Object> params = new HashMap<>();
        params.put("date", TOMORROW);
        params.put("timeId", 1);
        params.put("themeId", 1);
        params.put("memberId", 1);

        RestAssured.given().log().all()
                .header("Cookie", memberToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(401);
    }

    @Test
    @DisplayName("관리자 예약 생성 시, memberId가 존재하지 않으면 예외가 발생한다.")
    void createReservationByAdminWithNotFoundMemberId() {
        final String adminToken = getAdminToken();
        final Map<String, Object> params = new HashMap<>();
        params.put("date", TOMORROW);
        params.put("timeId", 1);
        params.put("themeId", 1);
        params.put("memberId", -1);

        RestAssured.given().log().all()
                .header("Cookie", adminToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(404);
    }

    private String getMemberToken() {
        return getLoginToken(MEMBER_EMAIL, MEMBER_PASSWORD);
    }

    private String getAdminToken() {
        return getLoginToken(ADMIN_EMAIL, ADMIN_PASSWORD);
    }

    private String getLoginToken(final String email, final String password) {
        final Map<String, Object> loginParams = new HashMap<>();
        loginParams.put("email", email);
        loginParams.put("password", password);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(loginParams)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().header("Set-Cookie").split(";")[0];
    }
}
