package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.lang.reflect.Field;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class TimeControllerTest {

    @Autowired
    private TimeController timeController;

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
    @DisplayName("컨트롤러에는 JdbcTemplate를 사용한 DB관련 로직이 존재하지 않는다.")
    void jdbcTemplateNotInjected() {
        boolean isJdbcTemplateInjected = false;

        for (Field field : timeController.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }

        assertThat(isJdbcTemplateInjected).isFalse();
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
