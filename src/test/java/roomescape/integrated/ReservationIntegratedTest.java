package roomescape.integrated;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain_entity.Reservation;
import roomescape.domain_entity.ReservationTime;
import roomescape.domain_entity.Theme;
import roomescape.dto.ReservationTimeRequest;
import roomescape.service.ReservationTimeService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationIntegratedTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private ReservationTimeDao timeDao;
    @Autowired
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("예약 관리 메인 페이지를 렌더링한다.")
    void displayMainAdmin() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @Disabled
    @DisplayName("예약을 생성한다.")
    void createReservation() {
        //given
        reservationTimeService.createTime(new ReservationTimeRequest(LocalTime.of(10, 0, 0)));

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2023-08-05");
        reservation.put("timeId", 1);

        //when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("id", is(1),
                        "name", is("브라운"),
                        "date", is("2023-08-05"),
                        "time.id", is(1),
                        "time.startAt", is("10:00")
                );

        //then
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @Disabled
    @DisplayName("전체 예약을 조회한다.")
    void readAllReservations() {
        createReservation();

        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @Disabled
    @DisplayName("예약을 삭제한다.")
    void deleteReservation() {
        createReservation();

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @Disabled
    @DisplayName("Read 요청 수행 시 DB 전체 데이터를 조회한다.")
    void retrieveReservationsWhenRead() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0, 0));
        timeDao.create(time);
        reservationDao.create(new Reservation(
                "브라운", LocalDate.of(2025, 4, 26), time,
                new Theme("moda", "description", "thumbnail")
        ));

        /**
         * todo : Id는 레코드임에도 불구하고 역직렬화 시 1이 역직렬화되지 않음
         */
        List<Reservation> reservations = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", Reservation.class);

        Integer count = jdbcTemplate.queryForObject("SELECT count(*) from reservation", Integer.class);

        assertThat(reservations.size()).isEqualTo(count);
    }

    @Test
    @Disabled
    @DisplayName("Post 요청 수행 시 Database에 예약 데이터 생성된다.")
    void addReservationDataWhenPost() {
        timeDao.create(new ReservationTime(LocalTime.of(10, 0, 0)));
        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2023-08-05");
        params.put("timeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM RESERVATION", Integer.class);
        assertThat(count).isEqualTo(1);
    }

    @Test
    @Disabled
    @DisplayName("Delete 요청 수행 시 Database에 예약 데이터 삭제한다.")
    void deleteReservationDataWhenDelete() {
        addReservationDataWhenPost();

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(200);

        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(*) from reservation", Integer.class);
        assertThat(countAfterDelete).isEqualTo(0);
    }
}
