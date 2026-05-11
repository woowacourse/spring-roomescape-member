package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 예약_목록을_조회한다() {
        ReservationTime time = reservationTimeRepository.save(ReservationTime.of("10:00"));
        Theme theme = themeRepository.save(Theme.of("공포", "desc", "url"));
        reservationRepository.save(Reservation.of("아이큐", "2025-06-01", time, theme));

        List<Map<String, Object>> reservations = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".");

        assertThat(reservations).hasSize(1);
        assertThat(reservations.get(0).get("name")).isEqualTo("아이큐");
    }

    @Test
    void 예약을_추가한다() {
        ReservationTime time = reservationTimeRepository.save(ReservationTime.of("10:00"));
        Theme theme = themeRepository.save(Theme.of("공포", "desc", "url"));

        Map<String, Object> params = Map.of(
                "name", "아이큐",
                "date", "2025-06-01",
                "timeId", time.getId(),
                "themeId", theme.getId()
        );

        Map<String, Object> result = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .extract().jsonPath().getMap(".");

        assertThat(result.get("name")).isEqualTo("아이큐");
    }

    @Test
    void 예약을_삭제한다() {
        ReservationTime time = reservationTimeRepository.save(ReservationTime.of("10:00"));
        Theme theme = themeRepository.save(Theme.of("공포", "desc", "url"));
        Reservation saved = reservationRepository.save(Reservation.of("아이큐", "2025-06-01", time, theme));

        RestAssured.given().log().all()
                .when().delete("/reservations/" + saved.getId())
                .then().log().all()
                .statusCode(200);

        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).isEmpty();
    }
}
