package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@ActiveProfiles("test")
@Sql(scripts = {"/schema.sql", "/test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ThemeApiTest {

    @Test
    void 테마를_추가한다() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "배트맨");
        params.put("description", "");
        params.put("thumbnail", "");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
            .when().post("/themes")
            .then().log().all()
            .statusCode(201);
    }

    @Test
    void 모든_테마를_조회한다() {
        RestAssured.given().log().all()
            .when().get("/themes")
            .then().log().all()
            .statusCode(200)
            .body("size()", is(3));
    }

    @Test
    void 이름은_null값을_받을_수_없다() {
        Map<String, String> params = new HashMap<>();
        params.put("name", null);
        params.put("description", "");
        params.put("thumbnail", "");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
            .when().post("/themes")
            .then().log().all()
            .statusCode(400);
    }

    @Test
    void 예약이_존재할_때_테마를_제거하면_에러를_반환한다() {
        RestAssured.given().log().all()
            .when().delete("/themes/1")
            .then().log().all()
            .statusCode(409)
            .body("detail", equalTo("이 시간의 예약이 존재합니다."));
    }
}
