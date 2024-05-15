package roomescape.integration;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

class MemberIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
    }

    @Test
    @DisplayName("모든 회원의 ID와 이름을 응답한다.")
    void readMembers() {
        jdbcTemplate.update(
                "INSERT INTO member (name, email, password, role) VALUES ('사용자1', 'user1@wooteco.com', 'user1', 'USER')");
        jdbcTemplate.update(
                "INSERT INTO member (name, email, password, role) VALUES ('사용자2', 'user2@wooteco.com', 'user2', 'USER')");

        RestAssured.given().log().all()
                .when().get("/members")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2))
                .body("[0].id", is(1))
                .body("[0].name", is("사용자1"));
    }
}
