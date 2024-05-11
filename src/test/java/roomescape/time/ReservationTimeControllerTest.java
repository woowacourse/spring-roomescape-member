package roomescape.time;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeDao;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeControllerTest {

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Test
    @DisplayName("저장된 모든 예약시간을 조회하고 상태코드 200을 응답한다.")
    void findAll() {
        insertReservationTime("09:00");
        insertReservationTime("10:00");
        assertReservationTimeCountIsEqualTo(2);

        List<ReservationTime> times = RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationTime.class);

        Integer count = reservationTimeDao.findAll().size();
        assertThat(times.size()).isEqualTo(count);
    }

    @Test
    @DisplayName("예약시간을 추가하고 상태코드 201을 응답한다.")
    void create() {
        insertReservationTime("11:00");
        assertReservationTimeCountIsEqualTo(1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTime("10:00"))
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        assertReservationTimeCountIsEqualTo(2);
    }

    @Test
    @DisplayName("저장된 예약시간을 삭제하고 상태코드 204을 응답한다.")
    void delete() {
        long id = insertReservationTimeAndGetId("13:00");
        assertReservationTimeCountIsEqualTo(1);

        RestAssured.given().log().all()
                .when().delete("/times/" + id)
                .then().log().all()
                .statusCode(204);

        assertReservationTimeCountIsEqualTo(0);
    }

    @Test
    @DisplayName("예약시간 추가 시 startAt의 형식이 잘못된 경우 상태코드 400을 응답한다.")
    void timeFormatException() {
        Map<String, String> request = new HashMap<>();
        request.put("id", "0");
        request.put("startAt", null);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }

    ReservationTime reservationTime(String time) {
        return new ReservationTime(0, LocalTime.parse(time));
    }

    void insertReservationTime(String time) {
        insertReservationTimeAndGetId(time);
    }

    long insertReservationTimeAndGetId(String time) {
        return reservationTimeDao.save(new ReservationTime(0, LocalTime.parse(time))).id();
    }

    void assertReservationTimeCountIsEqualTo(int count) {
        assertThat(count).isEqualTo(reservationTimeDao.findAll().size());
    }
}
