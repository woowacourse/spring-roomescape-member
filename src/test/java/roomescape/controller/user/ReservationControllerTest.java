package roomescape.controller.user;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import roomescape.auth.dto.TokenRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationControllerTest {

    private static final String EMAIL = "admin@admin.com";
    private static final String PASSWORD = "1234";

    String adminToken;
    String createdId;
    int reservationSize;

    @LocalServerPort
    int port;

    @DisplayName("어드민의 예약 CRUD")
    @TestFactory
    Stream<DynamicTest> dynamicTestsFromCollection() {
        RestAssured.port = port;

        return Stream.of(
                dynamicTest("어드민으로 로그인하기", () -> {
                    adminToken = RestAssured
                            .given().log().all()
                            .body(new TokenRequest(EMAIL, PASSWORD))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .when().post("/login")
                            .then().log().all().extract().header("Set-Cookie").split("=")[1];
                }),
                dynamicTest("예약을 목록을 조회한다.", () -> {
                    reservationSize = RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie("token", adminToken)
                            .when().get("/reservations")
                            .then().log().all()
                            .statusCode(200).extract()
                            .response().jsonPath().getList("$").size();
                }),
                dynamicTest("예약을 추가한다.", () -> {
                    Map<String, Object> params = Map.of(
                            "memberId", 1L,
                            "date", "2025-10-05",
                            "timeId", 1L,
                            "themeId", 1L);

                    createdId = RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie("token", adminToken)
                            .body(params)
                            .when().post("/admin/reservations")
                            .then().log().all()
                            .statusCode(201).extract().header("location").split("/")[2];
                }),
                dynamicTest("존재하지 않는 시간으로 예약을 추가할 수 없다.", () -> {
                    Map<String, Object> params = Map.of(
                            "memberId", 1L,
                            "date", "2025-10-05",
                            "timeId", 100L,
                            "themeId", 1L);

                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie("token", adminToken)
                            .body(params)
                            .when().post("/admin/reservations")
                            .then().log().all()
                            .statusCode(400);
                }),
                dynamicTest("존재하지 않는 테마로 예약을 추가할 수 없다.", () -> {
                    Map<String, Object> params = Map.of(
                            "memberId", 1L,
                            "date", "2025-10-05",
                            "timeId", 1L,
                            "themeId", 100L);

                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie("token", adminToken)
                            .body(params)
                            .when().post("/admin/reservations")
                            .then().log().all()
                            .statusCode(400);
                }),
                dynamicTest("존재하지 않는 회원으로 예약을 추가할 수 없다.", () -> {
                    Map<String, Object> params = Map.of(
                            "memberId", 100L,
                            "date", "2025-10-05",
                            "timeId", 1L,
                            "themeId", 1L);

                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie("token", adminToken)
                            .body(params)
                            .when().post("/admin/reservations")
                            .then().log().all()
                            .statusCode(400);
                }),
                dynamicTest("예약을 목록을 조회한다.", () -> {
                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie("token", adminToken)
                            .when().get("/reservations")
                            .then().log().all()
                            .statusCode(200).body("size()", is(reservationSize + 1));
                }),
                dynamicTest("예약을 삭제한다.", () -> {
                    System.out.println(createdId);
                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie("token", adminToken)
                            .when().delete("/reservations/" + createdId)
                            .then().log().all()
                            .statusCode(204);
                }),
                dynamicTest("이미 삭제된 예약을 삭제시도하면 statusCode가 400이다.", () -> {
                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie("token", adminToken)
                            .when().delete("/reservations/" + createdId)
                            .then().log().all()
                            .statusCode(400);
                })
        );
    }
}
