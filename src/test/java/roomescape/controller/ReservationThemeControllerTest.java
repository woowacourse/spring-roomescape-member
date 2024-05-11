package roomescape.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/test_schema.sql", "/test_data.sql"})
public class ReservationThemeControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("테마 조회 요청 시 200으로 응답한다.")
    @Test
    void themesTest() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }

    @DisplayName("정상적인 테마 추가 요청 시 201으로 응답한다.")
    @Test
    void insertTest() throws JsonProcessingException {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "레벨3 탈출");
        params.put("description", "우테코 레벨3를 탈출하는 내용입니다.");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(params);

        RestAssured.given().contentType("application/json").body(requestJson).log().all()
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("테마 삭제 요청 시 204로 응답한다.")
    @Test
    void deleteTest() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "레벨3 탈출");
        params.put("description", "우테코 레벨3를 탈출하는 내용입니다.");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");


        int id = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .extract().path("id");

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(id));

        RestAssured.given().log().all()
                .when().delete("/themes/" + id)
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("예약이 존재하는 테마는 삭제할 수 없다.")
    @Test
    void invalidDeleteTimeTest() {
        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(400)
                .body(is("예약이 존재하는 테마는 삭제할 수 없습니다."));
    }

    @DisplayName("최근 일주일간 가장 예약이 많은 테마를 조회한다.")
    @Test
    void weeklyBestThemesTest() {
        RestAssured.given().log().all()
                .when().get("/best-themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }
}
