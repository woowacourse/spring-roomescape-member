package roomescape.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.reservation.controller.ReservationController;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationDao;
import roomescape.reservation.request.ReservationRequest;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeDao;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeDao;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private ReservationController reservationController;

    @Test
    @DisplayName("저장된 모든 예약을 조회하고 상태코드 200을 응답한다.")
    void findAll() {
        long timeId = insertReservationTimeAndGetId("10:00");
        long themeId = insertThemeAndGetId("name", "description", "thumbnail");
        insertReservation("브라운", "2024-08-05", timeId, themeId);
        insertReservation("구구", "2024-08-06", timeId, themeId);
        assertReservationCountIsEqualTo(2);

        List<Reservation> reservations = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", Reservation.class);

        Integer count = reservationDao.findAll().size();
        assertThat(reservations.size()).isEqualTo(count);
    }

    @Test
    @DisplayName("예약을 추가하고 상태코드 201을 응답한다.")
    void create() {
        long themeId = insertThemeAndGetId("name", "description", "thumbnail");
        long timeId = insertReservationTimeAndGetId("10:00");

        insertReservation("브라운", "2024-08-05", timeId, themeId);
        assertReservationCountIsEqualTo(1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest("구구", "2024-08-06", timeId, themeId))
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        assertReservationCountIsEqualTo(2);
    }

    @Test
    @DisplayName("저장된 예약을 삭제하고 상태코드 204을 응답한다.")
    void delete() {
        long themeId = insertThemeAndGetId("name", "description", "thumbnail");
        long timeId = insertReservationTimeAndGetId("10:00");
        long id = insertReservationAndGetId("브라운", "2024-08-05", timeId, themeId);

        RestAssured.given().log().all()
                .when().delete("/reservations/" + id)
                .then().log().all()
                .statusCode(204);

        assertReservationCountIsEqualTo(0);
    }

    @Test
    @DisplayName("컨트롤러와 DB 관련 로직 클래스의 분리를 확인한다.")
    void layeredStructure() {
        boolean isJdbcTemplateInjected = false;

        for (Field field : reservationController.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }

        assertThat(isJdbcTemplateInjected).isFalse();
    }

    ReservationRequest reservationRequest(String name, String date, long timeId, long themeId) {
        return new ReservationRequest(name, LocalDate.parse(date), timeId, themeId);
    }

    long insertReservationTimeAndGetId(String time) {
        return reservationTimeDao.save(new ReservationTime(0, LocalTime.parse(time))).id();
    }

    void insertReservation(String name, String date, long timeId, long themeId) {
        insertReservationAndGetId(name, date, timeId, themeId);
    }

    void insertTheme(String name, String description, String thumbnail) {
        insertThemeAndGetId(name, description, thumbnail);
    }

    long insertThemeAndGetId(String name, String description, String thumbnail) {
        return themeDao.save(new Theme( name, description, thumbnail)).id();
    }

    long insertReservationAndGetId(String name, String date, long timeId, long themeId) {
        ReservationTime time = reservationTimeDao.findById(timeId);
        Theme theme = themeDao.findById(themeId);
        return reservationDao.save(new Reservation(name, LocalDate.parse(date), time, theme)).id();
    }

    void assertReservationCountIsEqualTo(int count) {
        assertThat(count).isEqualTo(reservationDao.findAll().size());
    }
}
