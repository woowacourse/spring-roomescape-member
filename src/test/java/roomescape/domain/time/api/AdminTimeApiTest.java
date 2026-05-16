package roomescape.domain.time.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

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
import roomescape.global.error.TypeMismatchMessage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("관리자 시간의")
class AdminTimeApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Nested
    @DisplayName("시간 조회 api는")
    class GetTimesTest {

        @Test
        @DisplayName("전체 시간을 조회한다.")
        void 성공() {
            given()
                .when()
                .get("/api/admin/times")
                .then()
                .statusCode(200);
        }
    }

    @Nested
    @DisplayName("시간 생성 api는")
    class SaveTimeTest {

        @Test
        @DisplayName("시간을 생성한다.")
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

        @Test
        @DisplayName("필수 필드가 누락되면 400을 반환한다.")
        void 실패1() {
            given()
                .contentType(ContentType.JSON)
                .body(Map.of())
                .when()
                .post("/api/admin/times")
                .then()
                .statusCode(400)
                .body("errors.field", hasItem("startAt"))
                .body("errors.find { it.field == 'startAt' }.value", nullValue())
                .body("errors.find { it.field == 'startAt' }.message", notNullValue());
        }

        @Test
        @DisplayName("시간 형식이 잘못되면 400을 반환한다.")
        void 실패2() {
            String wrongStartAt = "23-30";

            given()
                .contentType(ContentType.JSON)
                .body(Map.of("startAt", wrongStartAt))
                .when()
                .post("/api/admin/times")
                .then()
                .statusCode(400)
                .body("errors.field", hasItem("startAt"))
                .body("errors.find { it.field == 'startAt' }.value", equalTo(wrongStartAt))
                .body("errors.find { it.field == 'startAt' }.message", equalTo("형식이 올바르지 않습니다."));
        }
    }

    @Nested
    @DisplayName("시간 삭제 api는")
    class DeleteTimeTest {

        @Test
        @DisplayName("시간을 삭제한다.")
        void 성공() {
            Long id = createTime();

            given()
                .when()
                .delete("/api/admin/times/{id}", id)
                .then()
                .statusCode(204);
        }

        @Test
        @DisplayName("요청 경로 변수가 잘못되면 400을 반환한다.")
        void 실패1() {
            Object wrongId = "a";

            given()
                .when()
                .delete("/api/admin/times/{id}", wrongId)
                .then()
                .statusCode(400)
                .body("errors.field", hasItem("id"))
                .body("errors.find { it.field == 'id' }.value", equalTo(wrongId))
                .body("errors.find { it.field == 'id' }.message", equalTo(TypeMismatchMessage.from(Long.class)));
        }

        @Test
        @DisplayName("id가 1보다 작으면 400을 반환한다.")
        void 실패2() {
            given()
                .when()
                .delete("/api/admin/times/{id}", 0)
                .then()
                .statusCode(400)
                .body("errors.field", hasItem("id"))
                .body("errors.find { it.field == 'id' }.value", equalTo("0"))
                .body("errors.find { it.field == 'id' }.message", notNullValue());
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
