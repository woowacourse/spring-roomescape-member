package roomescape.integration.admin;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.MemberRole;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MemberApiTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("모든 일반 회원을 조회할 수 있다")
    @Test
    void canGetAllMembers() {
        // given
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?,?,?,?)",
                "회원1", "test1@test.com", "ecxewqe!23", MemberRole.GENERAL.toString());
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?,?,?,?)",
                "회원2", "test2@test.com", "ecxewqe!23", MemberRole.GENERAL.toString());
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?,?,?,?)",
                "관리자1", "test3@test.com", "ecxewqe!23", MemberRole.ADMIN.toString());

        // when & then
        RestAssured
                .given().log().all()
                .when().get("/members")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));

    }

    @DisplayName("일반 회원을 추가할 수 있다")
    @Test
    void canAddMember() {
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("email", "test@test.com");
        params.put("password", "asdf1234!");
        params.put("name", "member");

        // when & then
        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/members")
                .then().log().all()
                .statusCode(201)
                .header("location", "/member/1");
    }
}
