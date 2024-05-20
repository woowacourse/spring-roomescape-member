package roomescape.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

    @LocalServerPort
    int serverPort;

    @BeforeEach
    public void beforeEach() {
        RestAssured.port = serverPort;
    }

    private String accessToken;

    @Test
    @DisplayName("예약 내역 조회 API 작동을 확인한다")
    void checkReservations() {
        RestAssured.given().log().all()
                .when().get("reservations")
                .then().log().all()
                .statusCode(200).body("size()", is(0));
    }

    @TestFactory
    @DisplayName("예약 추가와 삭제의 작동을 확인한다")
    Stream<DynamicTest> reservationCreateAndDelete() {
        Map<String, String> reservationParams = Map.of(
                "date", "2025-08-05",
                "timeId", "1",
                "themeId", "1"
        );

        Map<String, String> login = Map.of(
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
                }),

                dynamicTest("id가 1인 예약을 삭제한다", () -> {
                    RestAssured.given().log().all()
                            .when().delete("/reservations/1")
                            .then().log().all()
                            .statusCode(204);
                }),

                dynamicTest("예약이 정상적으로 삭제되었는지 확인한다", () -> {
                    RestAssured.given().log().all()
                            .when().get("/reservations")
                            .then().log().all()
                            .statusCode(200).body("size()", is(0));
                })
        );
    }

    @TestFactory
    @DisplayName("예약자명이 잘못된 경우 응답 코드 400을 반환한다.")
    Stream<DynamicTest> checkReservationMemberName() {

        Map<String, String> reservationParams = Map.of(
                "name", "초롱!!",
                "date", "2023-08-05",
                "timeId", "1",
                "themeId", "1"
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
                dynamicTest("예약 시간을 추가한다.", () -> {
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
                            .contentType(ContentType.JSON).body(reservationParams)
                            .when().post("/reservations")
                            .then().log().all()
                            .statusCode(400);
                })
        );
    }

    @TestFactory
    @DisplayName("예약 날짜가 누락된 경우 응답 코드 400을 반환한다.")
    Stream<DynamicTest> checkReservationDate() {

        Map<String, String> reservationParams = Map.of(
                "name", "초롱",
                "date", "",
                "timeId", "1",
                "themeId", "1"
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
                dynamicTest("예약 시간을 추가한다.", () -> {
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
                            .contentType(ContentType.JSON).body(reservationParams)
                            .when().post("/reservations")
                            .then().log().all()
                            .statusCode(400);
                })
        );
    }

    @TestFactory
    @DisplayName("예약 시간이 누락된 경우 응답 코드 400을 반환한다.")
    Stream<DynamicTest> checkReservationTime() {

        Map<String, String> reservationParams = Map.of(
                "name", "초롱",
                "date", "2024-10-10",
                "timeId", "시간 선택",
                "themeId", "1"
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
                dynamicTest("예약 시간을 추가한다.", () -> {
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
                            .contentType(ContentType.JSON).body(reservationParams)
                            .when().post("/reservations")
                            .then().log().all()
                            .statusCode(400);
                })
        );
    }

    @TestFactory
    @DisplayName("예약 시간이 현재 시간보다 이전 시간이면 예약이 불가능한지 확인한다.")
    Stream<DynamicTest> checkIsPassedReservationTime() {

        Map<String, String> reservationParams = Map.of(
                "name", "초롱",
                "date", "2020-05-01",
                "timeId", "1",
                "themeId", "1"
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
                dynamicTest("예약 시간을 추가한다.", () -> {
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
                            .contentType(ContentType.JSON).body(reservationParams)
                            .when().post("/reservations")
                            .then().log().all()
                            .statusCode(400);
                })
        );
    }

    @TestFactory
    @DisplayName("중복된 시간에 예약이 불가능한지 확인한다.")
    Stream<DynamicTest> checkDuplicatedReservationDateTime() {

        Map<String, String> reservationParams1 = Map.of(
                "date", "2025-05-01",
                "timeId", "1",
                "themeId", "1"
        );

        Map<String, String> reservationParams2 = Map.of(
                "date", "2025-05-01",
                "timeId", "1",
                "themeId", "1"
        );

        Map<String, String> login = Map.of(
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

                dynamicTest("예약 시간을 추가한다.", () -> {
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
                            .contentType(ContentType.JSON).body(reservationParams1)
                            .when().post("/reservations")
                            .then().log().all()
                            .statusCode(201);
                }),

                dynamicTest("중복된 시간에 예약을 추가한다", () -> {
                    RestAssured.given().log().all()
                            .cookie("token", accessToken)
                            .contentType(ContentType.JSON).body(reservationParams2)
                            .when().post("/reservations")
                            .then().log().all()
                            .statusCode(400);
                })
        );
    }

    @Test
    @DisplayName("존재하지 않는 예약의 삭제가 불가능한지 확인한다")
    void checkNotExistReservationDelete() {
        RestAssured.given().log().all()
                .when().delete("reservations/1")
                .then().log().all()
                .statusCode(400);
    }

    @TestFactory
    @DisplayName("예약 가능한 시간을 조회한다")
    Stream<DynamicTest> checkAvailableReservationTime() {
        Map<String, String> reservationParams = Map.of(
                "date", "2025-08-05",
                "timeId", "1",
                "themeId", "1"
        );

        Map<String, String> login = Map.of(
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

                dynamicTest("예약 가능 시간을 조회한다.", () -> {
                    RestAssured.given().log().all()
                            .when().get("/reservations/available?date=2025-08-05&themeId=1")
                            .then().log().all()
                            .statusCode(200).body("size()", is(1));
                })
        );
    }
}
