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
import roomescape.member.repository.MemberDao;
import roomescape.reservation.controller.ReservationController;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationDao;
import roomescape.reservation.request.ReservationRequest;
import roomescape.reservation.response.ReservationResponse;
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
    private MemberDao memberDao;

    @Autowired
    private ReservationController reservationController;

    @Test
    @DisplayName("저장된 모든 예약을 조회하고 상태코드 200을 응답한다.")
    void findAll() {
        List<ReservationResponse> reservations = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationResponse.class);

        int count = reservationDao.findAll().size();
        assertThat(reservations.size()).isEqualTo(count);
    }

    @Test
    @DisplayName("예약을 추가하고 상태코드 201을 응답한다.")
    void create() {
        int count = count();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationRequest(LocalDate.now(),1,1,1))
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        assertThat(count()).isEqualTo(count+1);
    }

    @Test
    @DisplayName("저장된 예약을 삭제하고 상태코드 204을 응답한다.")
    void delete() {
        int count = count();

        RestAssured.given().log().all()
                .when().delete("/reservations/" + 1)
                .then().log().all()
                .statusCode(204);

        assertThat(count()).isEqualTo(count-1);
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

    int count(){
        return reservationDao.findAll().size();
    }
}
