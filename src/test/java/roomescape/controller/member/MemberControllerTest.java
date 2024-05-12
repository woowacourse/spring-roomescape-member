package roomescape.controller.member;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.member.SignupRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/test_schema.sql"})
class MemberControllerTest {

    @Autowired
    private MemberController memberController;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("모든 회원을 조회한다.")
    @Test
    void findAll() {
        memberController.createMember(new SignupRequest("email1@email.com", "password1", "name1"));
        memberController.createMember(new SignupRequest("email2@email.com", "password2", "name2"));

        RestAssured.given().log().all()
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
                .statusCode(201)
                .body("id", is(1));
    }
}
