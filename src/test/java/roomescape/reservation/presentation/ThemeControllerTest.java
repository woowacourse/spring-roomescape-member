package roomescape.reservation.presentation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ThemeControllerTest {
    @Test
    void 테마_저장_API_테스트() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "무서운게 딱 좋아");
        params.put("description", "무서운 분위기의 방탈출");
        params.put("thumbnailUrl", "https://example.com/theme.jpg");


        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }
}
