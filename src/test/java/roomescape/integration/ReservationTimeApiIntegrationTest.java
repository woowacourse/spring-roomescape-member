package roomescape.integration;

import static org.hamcrest.CoreMatchers.is;

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
class ReservationTimeApiIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private AccessToken accessToken;

    @BeforeEach
    void setUpData() {
        String memberSetUp = "insert into member (name, email, password, role) values ('moda', 'moda_email', 'moda_password', 'ADMIN')";
        String reservationTimeSetUp = "insert into reservation_time (start_at) values ('10:00')";
        jdbcTemplate.update(memberSetUp);
        jdbcTemplate.update(reservationTimeSetUp);

        Member member = new Member(1L, "moda", "moda_email", "moda_password", MemberRole.ADMIN);
        accessToken = new AccessToken(member);
    }

    @Test
    @DisplayName("시간을 생성한다.")
    void createTime() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "11:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .cookie("token", accessToken.getValue())
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("전체 시간 데이터를 조회한다.")
    void readTimes() {
        RestAssured.given().log().all()
                .cookie("token", accessToken.getValue())
                .when().get("/admin/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("시간을 삭제한다.")
    void deleteTime() {
        RestAssured.given().log().all()
                .cookie("token", accessToken.getValue())
                .when().delete("/admin/times/1")
                .then().log().all()
                .statusCode(204);
    }
}
