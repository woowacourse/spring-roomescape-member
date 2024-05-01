package roomescape.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.ReservationAddRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("insert into reservation_time values(1,'10:00')");
        LocalDate reservationDate = LocalDate.now().plusDays(2);
        jdbcTemplate.update("insert into reservation (name, date, time_id) values(?,?,?)", "브라운", reservationDate, 1);
    }

    @AfterEach
    void setDown() {
        jdbcTemplate.update("delete from reservation");
    }

    @DisplayName("예약 목록을 불러올 수 있다.")
    @Test
    void should_response_reservation_list_when_request_reservations() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("예약을 추가를 성공할 시, 201 ok를 응답한다,")
    @Test
    void should_add_reservation_when_post_request_reservations() {
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(
                LocalDate.now().plusDays(3L),
                "브라운",
                1L);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationAddRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("이름이 null인 예약을 추가할 시, 400 bad request를 응답한다,")
    @Test
    void should_response_bad_request_when_post_request_reservations_with_null_name() {
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(
                LocalDate.now().plusDays(3L),
                null,
                1L);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationAddRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("존재하는 리소스에 대한 삭제 요청시, 204 no content를 응답한다.")
    @Test
    void should_remove_reservation_when_delete_request_reservations_id() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("존재하지 않는 리소스에 대한 삭제 요청시, 500 Internel Server Error를 응답한다.")
    @Test
    void should_response_bad_request_when_nonExist_id() {
        RestAssured.given().log().all()
                .when().delete("/reservations/2")
                .then().log().all()
                .statusCode(500);
    }
}
