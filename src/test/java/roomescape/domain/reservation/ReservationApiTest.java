package roomescape.domain.reservation;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.entity.Theme;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservation.repository.ReservationTimeRepository;
import roomescape.domain.reservation.repository.ThemeRepository;
import roomescape.domain.reservation.utils.JdbcTemplateUtils;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class ReservationApiTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    public static String formatDateTime(LocalDate dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return dateTimeFormatter.format(dateTime);
    }

    @BeforeEach
    void init() {
        JdbcTemplateUtils.deleteAllTables(jdbcTemplate);
    }

    @DisplayName("어드민 페이지로 접근할 수 있다.")
    @Test
    void test1() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민이 예약 관리 페이지에 접근한다.")
    @Test
    void test2() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("모든 예약 정보를 반환한다.")
    @Test
    void test3() {
        // given
        LocalTime time = LocalTime.of(15, 0);
        ReservationTime reservationTime = ReservationTime.withoutId(time);
        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);
        Theme theme = Theme.withoutId("공포", "우테코 공포",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        Theme savedTheme = themeRepository.save(theme);

        Reservation reservation = Reservation.withoutId("꾹", LocalDate.now(), savedReservationTime, savedTheme);
        reservationRepository.save(reservation);

        // then
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("예약 정보를 추가한다.")
    @Test
    void test4() {
        // given
        ReservationTime reservationTime = ReservationTime.withoutId(LocalTime.now());
        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);

        Theme theme = Theme.withoutId("공포", "우테코 공포",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        Theme savedTheme = themeRepository.save(theme);

        LocalDate now = LocalDate.now();

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", formatDateTime(now.plusDays(1)));
        reservation.put("timeId", savedReservationTime.getId());
        reservation.put("themeId", savedTheme.getId());

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("존재하지 않는 예약 시간 ID 를 추가하면 예외를 반환한다.")
    @Test
    void test5() {

        Theme theme = Theme.withoutId("공포", "우테코 공포",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        Theme savedTheme = themeRepository.save(theme);

        LocalDate now = LocalDate.now();

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", formatDateTime(now.plusDays(1)));
        reservation.put("timeId", 1);
        reservation.put("themeId", savedTheme.getId());

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }

    @DisplayName("존재하지 않는 테마 ID 를 추가하면 예외를 반환한다.")
    @Test
    void notExistThemeId() {
        // given
        ReservationTime reservationTime = ReservationTime.withoutId(LocalTime.now());
        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);

        LocalDate now = LocalDate.now();

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", formatDateTime(now.plusDays(1)));
        reservation.put("timeId", savedReservationTime.getId());
        reservation.put("themeId", 1L);

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }

    @DisplayName("예약을 삭제한다.")
    @Test
    void test6() {
        // given
        String name = "브라운";
        LocalDate now = LocalDate.now();

        ReservationTime reservationTime = ReservationTime.withoutId(LocalTime.now());
        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);

        Theme theme = Theme.withoutId("공포", "우테코 공포",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        Theme savedTheme = themeRepository.save(theme);

        Reservation reservation = Reservation.withoutId(name, now, savedReservationTime, savedTheme);
        Reservation saved = reservationRepository.save(reservation);
        Long id = saved.getId();

        // when & then
        RestAssured.given().log().all()
                .when().delete("/reservations/" + id.intValue())
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("존재하지 않는 예약을 삭제할 경우 NOT_FOUND 반환")
    @Test
    void test7() {
        RestAssured.given().log().all()
                .when().delete("/reservations/4")
                .then().log().all()
                .statusCode(404);
    }

    @DisplayName("예약 가능한 시간을 반환한다")
    @Test
    void test9() {
        Theme theme = themeRepository.save(Theme.withoutId("공포", "공포", "www.m.com"));
        ReservationTime time1 = reservationTimeRepository.save(ReservationTime.withoutId(LocalTime.of(10, 0)));
        reservationTimeRepository.save(ReservationTime.withoutId(LocalTime.of(11, 0)));

        LocalDate date = LocalDate.now();
        reservationRepository.save(Reservation.withoutId("꾹", date, time1, theme));

        String path = UriComponentsBuilder.fromUriString("/reservations/available")
                .queryParam("date", formatDateTime(date))
                .queryParam("themeId", theme.getId())
                .build()
                .toUriString();

        RestAssured.given().log().all()
                .when().get(path)
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2))
                .body("alreadyBooked", containsInAnyOrder(true, false));
    }
}
