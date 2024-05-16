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
import roomescape.dto.request.MemberCreateRequest;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class MemberControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private String cookie;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        jdbcTemplate.update("INSERT INTO member(name, email, password) VALUES ('켬미', 'aaa@naver.com', '1111')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:00')");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마1', '설명1' ,'https://image.jpg')");
        jdbcTemplate.update("INSERT INTO reservation (date, member_id, time_id, theme_id) VALUES  ('2023-08-05', 1, 1, 1)");

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
                .extract().cookie("token");
    }

    @DisplayName("사용자 목록을 읽을 수 있다.")
    @Test
    void readMembers() {
        int size = RestAssured.given().log().all()
                .cookie("token", cookie)
                .when().get("/members")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getInt("size()");

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from member", Integer.class);

        assertThat(size).isEqualTo(count);
    }

    @DisplayName("사용자를 DB에 추가할 수 있다.")
    @Test
    void createMember() {
        MemberCreateRequest params = new MemberCreateRequest("호돌", "bbb@naver.com", "2222");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", cookie)
                .body(params)
                .when().post("/members")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/members/2");

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from member", Integer.class);
        assertThat(count).isEqualTo(2);
    }

}
