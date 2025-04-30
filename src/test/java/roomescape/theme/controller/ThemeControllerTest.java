package roomescape.theme.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.theme.domain.dto.ThemeReqDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ThemeControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("theme를 생성하면, 201 응답이 도착한다.")
    @Test
    public void createTheme() {
        ThemeReqDto dto = new ThemeReqDto("a", "b", "c");

        RestAssured.given().port(port).log().all()
            .contentType(ContentType.JSON).body(dto)
            .when().post("/themes")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("전체 Theme를 조회한다.")
    @Test
    public void findAllTheme() {
        RestAssured.given().port(port).log().all()
            .when().get("/themes")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("Theme를 삭제한다.")
    @Test
    public void deleteById() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "a", "b", "c");

        RestAssured.given().port(port).log().all()
            .when().delete("/themes/1")
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value());

        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM theme", Long.class);
        assertThat(count).isZero();
    }

}
