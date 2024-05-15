package roomescape.theme;

import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.dto.ThemeRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/schema.sql"})
public class ThemeAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ThemeDao themeDao;
    private final ThemeRequestDto requestDto = new ThemeRequestDto("정글 모험", "열대 정글의 심연을 탐험하세요.",
            "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("테마를 저장할 수 있다.")
    @Test
    void save() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when().post("/themes")
                .then().statusCode(201);
    }

    @DisplayName("모든 테마를 조회할 수 있다.")
    @Test
    void findAll() {
        themeDao.save(requestDto.toTheme());

        RestAssured.given()
                .when().get("/themes")
                .then().statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("특정 테마를 삭제할 수 있다.")
    @Test
    void delete() {
        themeDao.save(requestDto.toTheme());

        RestAssured.given()
                .when().delete("/themes/1")
                .then().statusCode(200);
    }
}
