package roomescape.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/truncate.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class TimeControllerTest {

    @LocalServerPort
    private int port;

    private final Map<String, String> params = Map.of(
            "startAt", "17:00"
    );

    @Test
    @DisplayName("처음으로 등록하는 시간의 id는 1이다.")
    void firstPost() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1))
                .header("Location", "/times/1");
    }

    @Test
    @DisplayName("아무 시간도 등록 하지 않은 경우, 시간 목록 조회 결과 개수는 0개이다.")
    void readEmptyTimes() {
        RestAssured.given().log().all()
                .port(port)
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("하나의 시간만 등록한 경우, 시간 목록 조회 결과 개수는 1개이다.")
    void readTimesSizeAfterFirstPost() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1))
                .header("Location", "/times/1");

        RestAssured.given().log().all()
                .port(port)
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("하나의 시간만 등록한 경우, 시간 삭제 뒤 시간 목록 조회 결과 개수는 0개이다.")
    void readTimesSizeAfterPostAndDelete() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1))
                .header("Location", "/times/1");

        RestAssured.given().log().all()
                .port(port)
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .port(port)
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("시간의 입력 형식이 올바르지 않으면 4xx 상태코드를 리턴한다.")
    void invalidTimeFormat() {
        Map<String, String> invalidTimeParam = Map.of(
                "startAt", "hihi");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(invalidTimeParam)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }
}
