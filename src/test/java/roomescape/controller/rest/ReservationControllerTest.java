package roomescape.controller.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.controller.rest.request.ReservationRequest;
import roomescape.domain.Reservation;
import roomescape.repository.ReservationDao;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private ReservationController reservationController;

    @Test
    @DisplayName("저장된 모든 예약을 조회하고 상태코드 200을 응답한다.")
    void getAll() {
        assertReservationCountIsEqualTo(5);

        List<Reservation> reservations = RestAssured.given()
                .when().get("/reservations")
                .then()
                .statusCode(200).extract()
                .jsonPath().getList(".", Reservation.class);

        Integer count = reservationDao.getAll().size();
        assertThat(reservations.size()).isEqualTo(count);
    }

    @Test
    @DisplayName("예약을 추가하고 상태코드 201을 응답한다.")
    void create() {
        assertReservationCountIsEqualTo(5);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(new ReservationRequest("구구", LocalDate.parse("2024-08-06"), 1, 1))
                .when().post("/reservations")
                .then()
                .statusCode(201);

        assertReservationCountIsEqualTo(6);
    }

    @Test
    @DisplayName("저장된 예약을 삭제하고 상태코드 204을 응답한다.")
    void delete() {
        assertReservationCountIsEqualTo(5);

        RestAssured.given()
                .when().delete("/reservations/" + 1)
                .then()
                .statusCode(204);

        assertReservationCountIsEqualTo(4);
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

    void assertReservationCountIsEqualTo(int count) {
        assertThat(count).isEqualTo(reservationDao.getAll().size());
    }
}
