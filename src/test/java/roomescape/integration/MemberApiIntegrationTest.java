package roomescape.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.entity.AccessToken;
import roomescape.entity.Member;
import roomescape.entity.MemberRole;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MemberApiIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUpData() {
        String memberSetUp = "insert into member (name, email, password, role) values ('moda', 'moda_email', 'moda_password', 'USER')";
        jdbcTemplate.update(memberSetUp);
    }

    @Test
    @DisplayName("회원을 생성한다.")
    void postMember() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "pobi");
        params.put("email", "pobi_email");
        params.put("password", "pobi_password");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/members")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("회원을 삭제한다.")
    void deleteTimes() {
        Member member = new Member(1L, "moda", "moda_email", "moda_password", MemberRole.USER);
        AccessToken accessToken = new AccessToken(member);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", accessToken.getValue())
                .when().delete("/members")
                .then().log().all()
                .statusCode(204)
                .cookie("token", "");
    }
}
