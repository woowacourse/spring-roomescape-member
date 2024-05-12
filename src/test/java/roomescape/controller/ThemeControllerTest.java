package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.dto.request.ThemeCreateRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ThemeControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private String cookie;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        jdbcTemplate.update("INSERT INTO member(name, email, password) VALUES ('켬미', 'aaa@naver.com', '1111')");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "오리와 호랑이", "오리들과 호랑이들 사이에서 살아남기", "https://image.jpg");

        Map<String, String> admin = Map.of(
                "email", "aaa@naver.com",
                "password", "1111"
        );

        cookie = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(admin)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().header("Set-Cookie").split(";")[0];
    }

    @DisplayName("테마 목록을 읽을 수 있다.")
    @Test
    void readReservations() {
        int size = RestAssured.given().log().all()
                .header("cookie", cookie)
                .when().get("/themes")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getInt("size()");

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from theme", Integer.class);

        assertThat(size).isEqualTo(count);
    }

    @DisplayName("테마를 DB에 추가할 수 있다.")
    @Test
    void createTime() {
        ThemeCreateRequest params = new ThemeCreateRequest(
                "오리와 호랑이2", "오리들과 호랑이들 사이에서 살아남기2", "https://image.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .header("cookie", cookie)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/themes/2");

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from theme", Integer.class);
        assertThat(count).isEqualTo(2);
    }

    @DisplayName("삭제할 id를 받아서 DB에서 해당 테마를 삭제 할 수 있다.")
    @Test
    void deleteTime() {
        RestAssured.given().log().all()
                .header("cookie", cookie)
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);

        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from theme", Integer.class);
        assertThat(countAfterDelete).isEqualTo(0);
    }
}
