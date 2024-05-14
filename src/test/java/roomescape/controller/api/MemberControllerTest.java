package roomescape.controller.api;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.domain.Role;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
class MemberControllerTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("회원을 가입한다.")
    void signup() {
        final Map<String, String> params = new HashMap<>();
        params.put("name", "jojo");
        params.put("email", "jojo@gmail.com");
        params.put("password", "11111");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/members")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("회원가입 시, 이메일이 중복되면 예외가 발생한다.")
    void signupWithDuplicatedEmail() {
        final Map<String, String> params = new HashMap<>();
        params.put("name", "jojojo");
        params.put("email", "imjojo@gmail.com");
        params.put("password", "11111");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/members")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("모든 회원 목록을 조회한다.")
    void findAllRoleMembers() {
        RestAssured.given().log().all()
                .when().get("/members")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(countMember()));
    }

    private int countMember() {
        final String query = "SELECT count(*) FROM member WHERE role = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, Role.MEMBER.name());
    }
}
