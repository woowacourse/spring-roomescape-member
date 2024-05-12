package roomescape.controller.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import roomescape.domain.Reservation;
import roomescape.repository.ReservationDao;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestInstance(Lifecycle.PER_METHOD) // TODO: 테스트 성능 개선
class ReservationControllerTest {

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private ReservationController reservationController;

    @Test
    @DisplayName("저장된 모든 예약을 조회하고 상태코드 200을 응답한다.")
    void findAll() {
        assertReservationCountIsEqualTo(5);

        List<Reservation> reservations = RestAssured.given()
                .when().get("/reservations")
                .then()
                .statusCode(200).extract()
                .jsonPath().getList(".", Reservation.class);

        Integer count = reservationDao.findAll().size();
        assertThat(reservations.size()).isEqualTo(count);
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
        assertThat(count).isEqualTo(reservationDao.findAll().size());
    }
}
