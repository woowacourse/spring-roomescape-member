package roomescape.reservation.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.fixture.ui.LoginApiFixture;
import roomescape.fixture.ui.MemberApiFixture;
import roomescape.fixture.ui.ReservationTimeApiFixture;
import roomescape.fixture.ui.ThemeApiFixture;
import roomescape.reservation.ui.dto.response.AvailableReservationTimeResponse;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationRestControllerTest {

    private final Map<String, String> signupParams1 = MemberApiFixture.signUpParams1();
    private final Map<String, String> signupParams2 = MemberApiFixture.signUpParams2();
    private final String date = LocalDate.now().plusDays(1).toString();

    @BeforeEach
    void setUp() {
        final Map<String, String> adminCookies = LoginApiFixture.adminLoginAndGetCookies();
        // 관리자 권한으로 예약 시간 추가 (3개)
        final Map<String, String> reservationTimeParams1 = ReservationTimeApiFixture.reservationTimeParams1();
        final Map<String, String> reservationTimeParams2 = ReservationTimeApiFixture.reservationTimeParams2();
        final Map<String, String> reservationTimeParams3 = ReservationTimeApiFixture.reservationTimeParams3();
        RestAssured.given().log().all()
                .cookies(adminCookies)
                .contentType(ContentType.JSON)
                .body(reservationTimeParams1)
                .when().post("/times")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
        RestAssured.given().log().all()
                .cookies(adminCookies)
                .contentType(ContentType.JSON)
                .body(reservationTimeParams2)
                .when().post("/times")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
        RestAssured.given().log().all()
                .cookies(adminCookies)
                .contentType(ContentType.JSON)
                .body(reservationTimeParams3)
                .when().post("/times")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        // 테마 추가 (2개)
        final Map<String, String> themeParams1 = ThemeApiFixture.themeParams1();
        final Map<String, String> themeParams2 = ThemeApiFixture.themeParams2();
        RestAssured.given().log().all()
                .cookies(adminCookies)
                .contentType(ContentType.JSON)
                .body(themeParams1)
                .when().post("/themes")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
        RestAssured.given().log().all()
                .cookies(adminCookies)
                .contentType(ContentType.JSON)
                .body(themeParams2)
                .when().post("/themes")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        // 회원 추가 (2명)
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(signupParams1)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(signupParams2)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }


    @Test
    void 예약을_추가한다() {
        final Map<String, String> cookies = LoginApiFixture.memberLoginAndGetCookies(signupParams1);
        final Map<String, String> reservationParams = reservationParams1();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .body(reservationParams)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void 로그인_상태가_아니면_예약을_추가할_수_없다() {
        final Map<String, String> reservationParams = reservationParams1();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void 예약을_삭제한다() {
        final Map<String, String> adminCookies = LoginApiFixture.adminLoginAndGetCookies();
        final Map<String, String> member1Cookies = LoginApiFixture.memberLoginAndGetCookies(signupParams1);
        final Map<String, String> reservationParams = reservationParams1();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookies(member1Cookies)
                .body(reservationParams)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured.given().log().all()
                .cookies(adminCookies)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(1));

        RestAssured.given().log().all()
                .cookies(adminCookies)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());

        RestAssured.given().log().all()
                .cookies(adminCookies)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(0));
    }

    @Test
    void 삭제할_예약이_없는_경우_not_found를_반환한다() {
        final Map<String, String> adminCookies = LoginApiFixture.adminLoginAndGetCookies();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookies(adminCookies)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void 예약_목록을_조회한다() {
        final Map<String, String> adminCookies = LoginApiFixture.adminLoginAndGetCookies();
        final Map<String, String> reservationParams = reservationParams1();

        final int sizeBeforeCreate = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookies(adminCookies)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().path("size()");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookies(adminCookies)
                .body(reservationParams)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        final int sizeAfterCreate = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookies(adminCookies)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .path("size()");

        Assertions.assertThat(sizeAfterCreate).isEqualTo(sizeBeforeCreate + 1);
    }

    @Test
    void 예약_가능한_시간_목록을_조회한다() {
        final Map<String, String> adminCookies = LoginApiFixture.adminLoginAndGetCookies();
        final Map<String, String> member1Cookies = LoginApiFixture.memberLoginAndGetCookies(signupParams1);
        final Map<String, String> reservationParams1 = reservationParams1();
        final Map<String, String> reservationParams2 = reservationParams2();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookies(adminCookies)
                .body(reservationParams1)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookies(member1Cookies)
                .body(reservationParams2)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        final List<AvailableReservationTimeResponse> availableReservationTimeResponses =
                RestAssured.given().log().all()
                        .queryParam("date", date)
                        .queryParam("themeId", "1")
                        .when().get("/reservations/available-times")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract().jsonPath()
                        .getList(".", AvailableReservationTimeResponse.class);

        final long count = availableReservationTimeResponses.stream()
                .filter(availableReservationTimeResponse -> !availableReservationTimeResponse.alreadyBooked())
                .count();

        assertThat(count).isEqualTo(1);
    }

    private Map<String, String> reservationParams1() {
        final String timeId = "1";
        final String themeId = "1";

        return createReservationParams(date, timeId, themeId);
    }

    private Map<String, String> reservationParams2() {
        final String timeId = "2";
        final String themeId = "1";

        return createReservationParams(date, timeId, themeId);
    }


    private Map<String, String> createReservationParams(
            final String date,
            final String timeId,
            final String themeId
    ) {
        return Map.ofEntries(
                Map.entry("date", date),
                Map.entry("timeId", timeId),
                Map.entry("themeId", themeId)
        );
    }
}
