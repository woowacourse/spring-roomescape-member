package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminPageControllerTest {

    @LocalServerPort
    int serverPort;

    @BeforeEach
    public void beforeEach() {
        RestAssured.port = serverPort;
    }

    private String accessToken;

    @TestFactory
    @DisplayName("방탈출 어드민 메인 페이지 조회를 확인한다")
    Stream<DynamicTest> showAdminMainPage() {
        Map<String, String> login = Map.of(
                "role", "admin",
                "email", "admin1@email.com",
                "password", "password"
        );

        return Stream.of(
                dynamicTest("로그인을 한다.", () -> {
                    accessToken = RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(login)
                            .when().post("/login")
                            .then().log().all()
                            .extract().cookie("token");
                }),

                dynamicTest("어드민 메인 페이지를 조회한다.", () -> {
                    RestAssured.given().log().all()
                            .cookie("token", accessToken)
                            .when().get("/admin")
                            .then().log().all()
                            .statusCode(200);
                })
        );
    }

    @TestFactory
    @DisplayName("관리자 권한이 없는 경우 admin 페이지에 접근할 수 없다")
    Stream<DynamicTest> unauthorizedAdminMainPage() {
        Map<String, String> login = Map.of(
                "role", "user",
                "email", "user1@email.com",
                "password", "password"
        );

        return Stream.of(
                dynamicTest("로그인을 한다.", () -> {
                    accessToken = RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(login)
                            .when().post("/login")
                            .then().log().all()
                            .extract().cookie("token");
                }),

                dynamicTest("어드민 메인 페이지를 조회한다.", () -> {
                    RestAssured.given().log().all()
                            .cookie("token", accessToken)
                            .when().get("/admin")
                            .then().log().all()
                            .statusCode(401);
                })
        );
    }

    @TestFactory
    @DisplayName("방탈출 예약 관리 페이지 조회를 확인한다")
    Stream<DynamicTest> showReservationPage() {
        Map<String, String> login = Map.of(
                "role", "admin",
                "email", "admin1@email.com",
                "password", "password"
        );

        return Stream.of(
                dynamicTest("로그인을 한다.", () -> {
                    accessToken = RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(login)
                            .when().post("/login")
                            .then().log().all()
                            .extract().cookie("token");
                }),

                dynamicTest("방탈출 예약 관리 페이지를 조회한다.", () -> {
                    RestAssured.given().log().all()
                            .cookie("token", accessToken)
                            .when().get("/admin/reservation")
                            .then().log().all()
                            .statusCode(200);
                })
        );

    }

    @TestFactory
    @DisplayName("방탈출 예약 시간 관리 페이지 조회를 확인한다")
    Stream<DynamicTest> showReservationTimePage() {
        Map<String, String> login = Map.of(
                "role", "admin",
                "email", "admin1@email.com",
                "password", "password"
        );

        return Stream.of(
                dynamicTest("로그인을 한다.", () -> {
                    accessToken = RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(login)
                            .when().post("/login")
                            .then().log().all()
                            .extract().cookie("token");
                }),

                dynamicTest("방탈출 예약 관리 페이지를 조회한다.", () -> {
                    RestAssured.given().log().all()
                            .cookie("token", accessToken)
                            .when().get("/admin/time")
                            .then().log().all()
                            .statusCode(200);
                })
        );

    }

    @TestFactory
    @DisplayName("방탈출 테마 관리 페이지 조회를 확인한다")
    Stream<DynamicTest> showThemePage() {
        Map<String, String> login = Map.of(
                "role", "admin",
                "email", "admin1@email.com",
                "password", "password"
        );

        return Stream.of(
                dynamicTest("로그인을 한다.", () -> {
                    accessToken = RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(login)
                            .when().post("/login")
                            .then().log().all()
                            .extract().cookie("token");
                }),

                dynamicTest("방탈출 예약 관리 페이지를 조회한다.", () -> {
                    RestAssured.given().log().all()
                            .cookie("token", accessToken)
                            .when().get("/admin/theme")
                            .then().log().all()
                            .statusCode(200);
                })
        );

    }

    @TestFactory
    @DisplayName("관리자 페이지에서 예약을 추가한다")
    Stream<DynamicTest> reservationAdminCreate() {
        Map<String, String> reservationParams = Map.of(
                "date", "2025-08-05",
                "timeId", "1",
                "themeId", "1",
                "memberId", "1"
        );

        Map<String, String> login = Map.of(
                "role", "admin",
                "email", "admin1@email.com",
                "password", "password"
        );

        Map<String, String> reservationTimeParams = Map.of(
                "id", "1",
                "startAt", "10:00"
        );

        Map<String, String> themeParams = Map.of(
                "id", "1",
                "name", "테마명",
                "description", "테마 설명",
                "thumbnail", "테마 이미지"
        );

        return Stream.of(
                dynamicTest("로그인을 한다.", () -> {
                    accessToken = RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(login)
                            .when().post("/login")
                            .then().log().all()
                            .extract().cookie("token");
                }),

                dynamicTest("예약 시간을 추가한다", () -> {
                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(reservationTimeParams)
                            .when().post("/times")
                            .then().log().all()
                            .statusCode(201);
                }),

                dynamicTest("테마를 추가한다.", () -> {
                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(themeParams)
                            .when().post("/themes")
                            .then().log().all()
                            .statusCode(201);
                }),

                dynamicTest("예약을 추가한다", () -> {
                    RestAssured.given().log().all()
                            .cookie("token", accessToken)
                            .contentType(ContentType.JSON).body(reservationParams)
                            .when().post("/reservations")
                            .then().log().all()
                            .statusCode(201)
                            .header("Location", "/reservations/1");
                }),

                dynamicTest("예약이 정상적으로 추가되었는지 확인한다", () -> {
                    RestAssured.given().log().all()
                            .when().get("/reservations")
                            .then().log().all()
                            .statusCode(200).body("size()", is(1));
                })
        );
    }
}
