package roomescape.controller.member;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.TestUtil;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/test_schema.sql", "/test_member.sql"})
class MemberControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("모든 회원을 조회한다.")
    @Test
    void findAll() {
        RestAssured.given().log().all()
                .cookie("token", TestUtil.getMemberToken())
                .when().get("/members")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @DisplayName("회원을 추가한다.")
    @Test
    void createMember() {
        Map<String, String> param = Map.of(
                "email", "email1@email.com",
                "password", "password1",
                "name", "name1"
        );

        RestAssured.given().log().all()
                .contentType("application/json")
                .body(param)
                .when().post("/members")
                .then().log().all()
                .statusCode(201);
    }
}
