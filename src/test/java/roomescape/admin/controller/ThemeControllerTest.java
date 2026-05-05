package roomescape.admin.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.execute("DELETE FROM theme");

        jdbcTemplate.update("INSERT INTO theme(name, description, image) VALUES (?, ?, ?)", "은하수", "은하수 테마방입니다.",
                "http.123.jpg");
        jdbcTemplate.update("INSERT INTO theme(name, description, image) VALUES (?, ?, ?)", "지구", "지구 테마방입니다.",
                "http.123.jpg");
    }

    @Test
    void 관리자_전체_테마_조회() throws Exception {
        RestAssured.given().log().all()
                .when().get("/admin/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));

    }

    @Test
    void 관리자_단일_테마_조회() {
        RestAssured.given().log().all()
                .when().get("/admin/themes/1")
                .then().log().all()
                .statusCode(200)
                .body("name", is("은하수"));
    }

    @Test
    void 관리자_테마_추가() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "수성");
        params.put("description", "수성 테마방입니다.");
        params.put("image", "http.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201)
                .and().body("name", is("수성"));
    }
}
