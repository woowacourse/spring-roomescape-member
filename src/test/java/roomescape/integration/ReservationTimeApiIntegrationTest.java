package roomescape.integration;

import static org.hamcrest.CoreMatchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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
    private AccessToken adminToken;

    @BeforeEach
    void setUpData() {
        String memberSetUp = "insert into member (name, email, password, role) values ('moda', 'moda_email', 'moda_password', 'ADMIN')";
        String reservationTimeSetUp = "insert into reservation_time (start_at) values ('10:00'), ('11:00')";
        jdbcTemplate.update(memberSetUp);
        jdbcTemplate.update(reservationTimeSetUp);

        Member admin = new Member(1L, "moda", "moda_email", "moda_password", MemberRole.ADMIN);
        adminToken = new AccessToken(admin);
    }

    @Test
    @DisplayName("이용 가능한 시간을 조회한다.")
    void readAvailableTimes() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .param("date", "2025-08-04")
                .param("themeId", 1)
                .cookie("token", adminToken.getValue())
                .when().get("/times/available")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }
}
