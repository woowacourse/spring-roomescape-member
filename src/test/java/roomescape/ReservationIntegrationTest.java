package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    private static final String FUTURE_DATE = LocalDate.now().plusDays(1)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    private static final String FUTURE_DATE2 = LocalDate.now().plusDays(2)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 예약_목록을_조회한다() {
        ReservationTime time = reservationTimeRepository.save(ReservationTime.of("10:00"));
        Theme theme = themeRepository.save(Theme.of("공포", "desc", "url"));
        reservationRepository.save(Reservation.of("아이큐", FUTURE_DATE, time, theme));

        List<Map<String, Object>> reservations = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".");

        assertThat(reservations).hasSize(1);
        assertThat(reservations.get(0).get("name")).isEqualTo("아이큐");
    }

    @Test
    void 이름으로_예약을_조회한다() {
        ReservationTime time = reservationTimeRepository.save(ReservationTime.of("10:00"));
        Theme theme = themeRepository.save(Theme.of("공포", "desc", "url"));
        reservationRepository.save(Reservation.of("아이큐", FUTURE_DATE, time, theme));
        reservationRepository.save(Reservation.of("브라운", FUTURE_DATE2, time, theme));

        List<Map<String, Object>> result = RestAssured.given().log().all()
                .when().get("/reservations?name=아이큐")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).get("name")).isEqualTo("아이큐");
    }

    @Test
    void 예약을_추가한다() {
        ReservationTime time = reservationTimeRepository.save(ReservationTime.of("10:00"));
        Theme theme = themeRepository.save(Theme.of("공포", "desc", "url"));

        Map<String, Object> params = Map.of(
                "name", "아이큐",
                "date", FUTURE_DATE,
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
    void 예약을_변경한다() {
        ReservationTime time1 = reservationTimeRepository.save(ReservationTime.of("10:00"));
        ReservationTime time2 = reservationTimeRepository.save(ReservationTime.of("11:00"));
        Theme theme = themeRepository.save(Theme.of("공포", "desc", "url"));
        Reservation saved = reservationRepository.save(Reservation.of("아이큐", FUTURE_DATE, time1, theme));

        Map<String, Object> updateParams = Map.of(
                "date", FUTURE_DATE2,
                "timeId", time2.getId()
        );

        Map<String, Object> result = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(updateParams)
                .when().patch("/reservations/" + saved.getId())
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getMap(".");

        assertThat(result.get("name")).isEqualTo("아이큐");
    }

    @Test
    void 과거_날짜로_변경시_400을_반환한다() {
        ReservationTime time = reservationTimeRepository.save(ReservationTime.of("10:00"));
        Theme theme = themeRepository.save(Theme.of("공포", "desc", "url"));
        Reservation saved = reservationRepository.save(Reservation.of("아이큐", FUTURE_DATE, time, theme));

        Map<String, Object> updateParams = Map.of(
                "date", "2020-01-01",
                "timeId", time.getId()
        );

        Map<String, Object> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(updateParams)
                .when().patch("/reservations/" + saved.getId())
                .then().log().all()
                .statusCode(400)
                .extract().jsonPath().getMap(".");

        assertThat(response.get("message")).isEqualTo("지나간 날짜·시간에는 예약할 수 없습니다.");
    }

    @Test
    void 예약을_삭제한다() {
        ReservationTime time = reservationTimeRepository.save(ReservationTime.of("10:00"));
        Theme theme = themeRepository.save(Theme.of("공포", "desc", "url"));
        Reservation saved = reservationRepository.save(Reservation.of("아이큐", FUTURE_DATE, time, theme));

        RestAssured.given().log().all()
                .when().delete("/reservations/" + saved.getId())
                .then().log().all()
                .statusCode(204);

        assertThat(reservationRepository.findAll()).isEmpty();
    }
}
