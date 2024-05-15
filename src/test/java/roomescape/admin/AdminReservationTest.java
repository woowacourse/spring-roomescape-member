package roomescape.admin;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import java.util.HashMap;
import java.util.Map;

import static roomescape.fixture.MemberFixture.adminFixture;
import static roomescape.fixture.ReservationTimeFixture.reservationTimeFixture;
import static roomescape.fixture.ThemeFixture.themeFixture;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AdminReservationTest {

    private String token;

    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        insertReservationTime();
        insertTheme();
        adminLogIn();
    }

    private void insertTheme() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES(?, ?, ?)", themeFixture.getName(), themeFixture.getDescription(), themeFixture.getDescription());
    }

    private void insertReservationTime() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES(?)", reservationTimeFixture.getStartAt());
    }

    void adminLogIn() {
        Map<String, String> params = new HashMap<>();
        params.put("email", adminFixture.getEmail());
        params.put("password", adminFixture.getPassword());

        String cookie = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie");
        token = cookie.substring("token=".length(), cookie.indexOf(';'));
    }

    @DisplayName("어드민은 멤버 아이디로 예약을 추가할 수 있다.")
    @Test
    void reservationWithMemberId() {
        Map<String, String> param = new HashMap<>();
        param.put("date", "2999-12-12");
        param.put("timeId", "1");
        param.put("themeId", "1");
        param.put("memberId", "1");

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(param)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("예약 내역을 필터링할 수 있다.")
    @Test
    void filterReservations() {
        Map<String, String> param = new HashMap<>();
        param.put("memberId", "1");
        param.put("themeId", "1");
        param.put("dateFrom", "2000-12-12");
        param.put("dateTo", "2012-12-12");

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .queryParams(param)
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }
}
