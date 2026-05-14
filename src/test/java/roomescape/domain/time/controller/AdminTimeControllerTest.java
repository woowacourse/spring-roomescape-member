package roomescape.domain.time.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AdminTimeControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Nested
    @DisplayName("getTimes 테스트")
    class GetTimesTest {

        @Test
        @DisplayName("시간 목록 조회 요청이면 200을 반환한다.")
        void 성공() {
            given()
                .when()
                .get("/api/admin/times")
                .then()
                .statusCode(200);
        }
    }

    @Nested
    @DisplayName("saveTime 테스트")
    class SaveTimeTest {

        @Test
        @DisplayName("시간 생성 요청이면 201을 반환한다.")
        void 성공() {
            given()
                .contentType(ContentType.JSON)
                .body(Map.of("startAt", "23:30"))
                .when()
                .post("/api/admin/times")
                .then()
                .statusCode(201)
                .body("startAt", equalTo("23:30"));
        }
    }

    @Nested
    @DisplayName("deleteTime 테스트")
    class DeleteTimeTest {

        @Test
        @DisplayName("시간 삭제 요청이면 204를 반환한다.")
        void 성공() {
            Long id = createTime();

            given()
                .when()
                .delete("/api/admin/times/{id}", id)
                .then()
                .statusCode(204);
        }
    }

    private Long createTime() {
        return given()
            .contentType(ContentType.JSON)
            .body(Map.of("startAt", "23:30"))
            .when()
            .post("/api/admin/times")
            .then()
            .statusCode(201)
            .extract()
            .jsonPath()
            .getLong("id");
    }
}
