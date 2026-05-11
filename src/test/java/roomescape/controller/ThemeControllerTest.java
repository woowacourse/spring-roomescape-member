package roomescape.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.ThemeRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(statements = {
        "SET REFERENTIAL_INTEGRITY FALSE",
        "TRUNCATE TABLE reservation RESTART IDENTITY",
        "TRUNCATE TABLE reservation_time RESTART IDENTITY",
        "TRUNCATE TABLE theme RESTART IDENTITY",
        "SET REFERENTIAL_INTEGRITY TRUE"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/mockData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

public class ThemeControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void 테마_전체_조회_API() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("themes.size()", is(17));
    }

    @Test
    public void 테마_추가_API() {
        ThemeRequest themeRequest = new ThemeRequest("공포 영화", "테스트입니다", "url");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("size()", is(4));
    }

    @Test
    public void 특정_테마_삭제_API() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/themes/7")
                .then().log().all()
                .statusCode(204);
    }
}
