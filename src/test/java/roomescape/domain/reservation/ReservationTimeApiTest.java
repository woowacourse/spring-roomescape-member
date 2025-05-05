package roomescape.domain.reservation;

import static org.hamcrest.Matchers.is;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.entity.Theme;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservation.repository.ReservationTimeRepository;
import roomescape.domain.reservation.repository.ThemeRepository;
import roomescape.domain.reservation.utils.JdbcTemplateUtils;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeApiTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @AfterEach
    void tearDown() {
        JdbcTemplateUtils.deleteAllTables(jdbcTemplate);
    }

    @DisplayName("예약 시간을 추가한다.")
    @Test
    void test1() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("예약 시간 요청에 초가 있으면 Bad Request 반환")
    @Test
    void test2() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00:20");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("예약 시간을 가져온다")
    @Test
    void test3() {
        // given
        ReservationTime reservationTime = ReservationTime.withoutId(LocalTime.now());

        reservationTimeRepository.save(reservationTime);

        // when & then
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("해당 예약 시간을 삭제한다")
    @Test
    void test4() {
        // given
        ReservationTime reservationTime = ReservationTime.withoutId(LocalTime.now());

        ReservationTime saved = reservationTimeRepository.save(reservationTime);

        Long savedId = saved.getId();

        // when & then
        RestAssured.given().log().all()
                .when().delete("/times/" + savedId.intValue())
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("없는 예약 시간을 삭제하면 NOT FOUND 반환")
    @Test
    void test5() {
        // given
        int notFoundStatusCode = 404;

        // when & then
        RestAssured.given().log().all()
                .when().delete("/times/4")
                .then().log().all()
                .statusCode(notFoundStatusCode);
    }

    @DisplayName("사용 중인 예약 시간이 있다면 삭제를 하면 409 CONFLICT를 반환한다.")
    @Test
    void test6() {
        // given
        int conflictStatusCode = 409;
        ReservationTime reservationTime = ReservationTime.withoutId(LocalTime.now());

        ReservationTime savedTime = reservationTimeRepository.save(reservationTime);
        Long savedId = savedTime.getId();

        Theme theme = Theme.withoutId("공포", "우테코 공포",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        Theme savedTheme = themeRepository.save(theme);

        Reservation reservation = Reservation.withoutId("꾹", LocalDate.now(), savedTime, savedTheme);
        reservationRepository.save(reservation);

        // when & then
        RestAssured.given().log().all()
                .when().delete("/times/" + savedId.intValue())
                .then().log().all()
                .statusCode(conflictStatusCode);
    }
}
