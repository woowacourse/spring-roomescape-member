package roomescape.domain.reservation.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import io.restassured.RestAssured;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.TimeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminReservationApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
    }

    @Nested
    class GetReservationsTest {

        @Test
        void RL1_예약_목록을_조회한다() {
            // given
            Time time1 = timeRepository.save(Time.create(LocalTime.of(10, 0)));
            Time time2 = timeRepository.save(Time.create(LocalTime.of(11, 30)));
            Theme theme = themeRepository.save(Theme.create("테마1", "설명1", "image1.png"));
            Reservation reservation1 = reservationRepository.save(
                Reservation.create("예약자1", LocalDate.of(2026, 5, 1), time1, theme));
            Reservation reservation2 = reservationRepository.save(
                Reservation.create("예약자2", LocalDate.of(2026, 5, 2), time2, theme));

            // when & then
            given()
                .when().get("/api/admin/reservations")
                .then()
                .statusCode(200)
                .body("", hasSize(2))
                .body("[0].id", equalTo(reservation1.getId().intValue()))
                .body("[0].name", equalTo("예약자1"))
                .body("[0].date", equalTo("2026-05-01"))
                .body("[0].time.id", equalTo(time1.getId().intValue()))
                .body("[0].time.startAt", equalTo("10:00"))
                .body("[0].theme.id", equalTo(theme.getId().intValue()))
                .body("[0].theme.name", equalTo("테마1"))
                .body("[0].theme.description", equalTo("설명1"))
                .body("[0].theme.imageUrl", equalTo("image1.png"))
                .body("[1].id", equalTo(reservation2.getId().intValue()))
                .body("[1].name", equalTo("예약자2"))
                .body("[1].date", equalTo("2026-05-02"))
                .body("[1].time.id", equalTo(time2.getId().intValue()))
                .body("[1].time.startAt", equalTo("11:30"));
        }

        @Test
        void RL1_예약이_없으면_빈_목록을_반환한다() {
            // when & then
            given()
                .when().get("/api/admin/reservations")
                .then()
                .statusCode(200)
                .body("", empty());
        }
    }
}
