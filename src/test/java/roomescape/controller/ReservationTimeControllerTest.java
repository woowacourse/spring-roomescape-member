package roomescape.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import roomescape.IntegrationTestSupport;
import roomescape.controller.dto.TokenRequest;

class ReservationTimeControllerTest extends IntegrationTestSupport {

    String token;
    String createdId;
    int timeSize;

    @LocalServerPort
    int port;

    @DisplayName("예약 시간 CRUD")
    @TestFactory
    Stream<DynamicTest> dynamicUserTestsFromCollection() {
        RestAssured.port = port;

        return Stream.of(
                dynamicTest("어드민으로 로그인", () -> {
                    token = RestAssured
                            .given().log().all()
                            .body(new TokenRequest(ADMIN_EMAIL, ADMIN_PASSWORD))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .when().post("/login")
                            .then().log().all().extract().cookie("token");
                }),
                dynamicTest("예약 시간 목록을 조회한다.", () -> {
                    timeSize = RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie("token", token)
                            .when().get("/times")
                            .then().log().all()
                            .statusCode(200).extract()
                            .response().jsonPath().getList("$").size();
                }),
                dynamicTest("예약 시간을 추가한다.", () -> {
                    Map<String, String> param = Map.of("startAt", "12:12");

                    createdId = RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie("token", token)
                            .body(param)
                            .when().post("/admin/times")
                            .then().log().all()
                            .statusCode(201).extract().header("location").split("/")[2];
                }),
                dynamicTest("예약 시간 목록 개수가 1증가한다.", () -> {
                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie("token", token)
                            .when().get("/times")
                            .then().log().all()
                            .statusCode(200).body("size()", is(timeSize + 1));
                }),
                dynamicTest("유효하지 않은 형식으로 시간을 추가할 수 없다.", () -> {
                    Map<String, String> param = Map.of("startAt", "12:12:12");

                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie("token", token)
                            .body(param)
                            .when().post("/admin/times")
                            .then().log().all()
                            .statusCode(400);
                }),
                dynamicTest("시간을 삭제한다.", () -> {
                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie("token", token)
                            .when().delete("/admin/times/" + createdId)
                            .then().log().all()
                            .statusCode(204);
                }),
                dynamicTest("예약 시간 목록 개수가 1감소한다.", () -> {
                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie("token", token)
                            .when().get("/times")
                            .then().log().all()
                            .statusCode(200).body("size()", is(timeSize));
                })
        );
    }
}
