package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 시간_관리_API() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        int createdId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .extract().path("id");

        List<Map<String, Object>> times = RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".");

        assertThat(times).hasSize(1);
        assertThat(times.get(0).get("startAt")).isEqualTo("10:00");

        RestAssured.given().log().all()
                .when().delete("/times/" + createdId)
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 예약과_시간_연결() {
        ReservationTime time = reservationTimeRepository.save(ReservationTime.of("10:00"));
        Theme theme = themeRepository.save(Theme.of("공포", "무서워요", "https://zeze.com"));

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2023-08-05");
        reservation.put("timeId", time.getId());
        reservation.put("themeId", theme.getId());

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        List<Map<String, Object>> reservations = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".");

        assertThat(reservations).hasSize(1);
        assertThat(reservations.get(0).get("name")).isEqualTo("브라운");
    }
}
