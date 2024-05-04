package roomescape.integration;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "/clear.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class ReservationControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    private Theme theme1 = new Theme("theme1", "description", "thumbnail");
    private Theme theme2 = new Theme("theme2", "description", "thumbnail");

    private ReservationTime time1 = new ReservationTime(LocalTime.of(11, 30));
    private ReservationTime time2 = new ReservationTime(LocalTime.of(13, 40));

    @BeforeEach
    void initData() {
        RestAssured.port = port;

        theme1 = themeRepository.save(theme1);
        theme2 = themeRepository.save(theme2);

        time1 = reservationTimeRepository.save(time1);
        time2 = reservationTimeRepository.save(time2);
    }

    @DisplayName("예약이 10개 존재할 때")
    @Nested
    class ExistReservationTest {

        @BeforeEach
        void initData() {
            reservationRepository.save(new Reservation("name", LocalDate.now().plusDays(1), time1, theme1));
            reservationRepository.save(new Reservation("name", LocalDate.now().plusDays(1), time1, theme2));
            reservationRepository.save(new Reservation("name", LocalDate.now().plusDays(1), time2, theme1));
            reservationRepository.save(new Reservation("name", LocalDate.now().plusDays(1), time2, theme2));
            reservationRepository.save(new Reservation("name", LocalDate.now().plusDays(2), time1, theme1));

            reservationRepository.save(new Reservation("name", LocalDate.now().plusDays(2), time1, theme2));
            reservationRepository.save(new Reservation("name", LocalDate.now().plusDays(2), time2, theme1));
            reservationRepository.save(new Reservation("name", LocalDate.now().plusDays(2), time2, theme2));
            reservationRepository.save(new Reservation("name", LocalDate.now().plusDays(3), time1, theme1));
            reservationRepository.save(new Reservation("name", LocalDate.now().plusDays(3), time2, theme1));

        }

        @DisplayName("존재하는 모든 예약을 조회할 수 있다.")
        @Test
        void getReservationTest() {
            RestAssured.given().log().all()
                    .when().get("/reservations")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(10));
        }

        @DisplayName("예약을 하나 생성할 수 있다.")
        @Test
        void createReservationTest() {
            Map<String, Object> reservationParam = Map.of(
                    "name", "name",
                    "date", "2024-05-20",
                    "timeId", "1",
                    "themeId", "1");

            RestAssured.given().log().all()
                    .when()
                    .contentType(ContentType.JSON)
                    .body(reservationParam)
                    .post("/reservations")
                    .then().log().all()
                    .statusCode(201)
                    .body("name", is("name"));

            RestAssured.given().log().all()
                    .when().get("/reservations")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(11));
        }

        @DisplayName("예약을 하나 삭제할 수 있다.")
        @Test
        void deleteReservationTest() {

            RestAssured.given().log().all()
                    .when().delete("/reservations/1")
                    .then().log().all()
                    .statusCode(204);
        }
    }
}
