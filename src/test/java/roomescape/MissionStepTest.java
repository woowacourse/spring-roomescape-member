package roomescape;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.presentation.api.reservation.ReservationController;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {

    public static final String RESERVATION_DATE = LocalDate.now().plusDays(1).toString();
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationController reservationController;

    @Autowired
    private ReservationRepository reservationRepository;


    @Test
    void admin_경로로_요청을_보내면_어드민_메인_페이지를_응답할_수_있다() {
        String cookie = getAdminTokenCookie();
        RestAssured.given().log().all()
                .header("Cookie", cookie)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 예약_관리_페이지를_응답할_수_있다() {
        String cookie = getAdminTokenCookie();
        RestAssured.given().log().all()
                .header("Cookie", cookie)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 예약_목록을_조회할_수_있다() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void 스프링이_실행되면_reservation_테이블이_생성된다() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.getCatalog()).isEqualTo("TEST");
            assertThat(connection.getMetaData().getTables(null, null, "RESERVATION", null).next()).isTrue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 예약_시간을_생성_조회_삭제할_수_있다() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "12:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 구단계() {
        boolean isJdbcTemplateInjected = false;

        for (Field field : reservationController.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }

        assertThat(isJdbcTemplateInjected).isFalse();
    }

    private String getAdminTokenCookie() {
        jdbcTemplate.update("INSERT INTO member(name, email, password, role) VALUES (?, ?, ?, ?)", "member1",
                "email@gmail.com", "password", "ADMIN");
        Map<String, String> params = Map.of(
                "email", "email@gmail.com",
                "password", "password"
        );
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie").split(";")[0];
    }
}
