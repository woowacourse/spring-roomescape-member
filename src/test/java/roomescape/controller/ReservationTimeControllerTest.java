package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.dto.ReservationTimeRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationTimeControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        jdbcTemplate.update("delete from reservation");
        jdbcTemplate.update("delete from reservation_time");
    }

    @DisplayName("모든 예약 시간 조회 테스트")
    @Test
    void findAllReservationTime() {
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all().assertThat().statusCode(HttpStatus.OK.value());
    }

    @DisplayName("예약 시간 추가 테스트")
    @Test
    void createReservationTime() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationTimeRequest("10:00"))
                .when().post("/times")
                .then().log().all().assertThat().statusCode(HttpStatus.OK.value());
    }

    @DisplayName("예약 시간 삭제 성공 테스트")
    @Test
    void deleteReservationTImeSuccess() {
        //given
        jdbcTemplate.update("INSERT INTO reservation_time VALUES (1, '10:00')");
        //then
        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all().assertThat().statusCode(HttpStatus.OK.value());
    }

    @DisplayName("예약 시간 삭제 실패 테스트")
    @Test
    void deleteReservationTimeFail() {
        //given
        long invalidId = 0;
        //then
        RestAssured.given().log().all()
                .when().delete("/reservations/" + invalidId)
                .then().log().all().assertThat().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("예약 시간 삭제 시, 해당 id를 참조하는 예약도 삭제된다.")
    @Test
    void deleteReservationTimeDeletesReservationAlso() {
        //given
        jdbcTemplate.update("INSERT INTO reservation_time VALUES (1, '10:00')");
        jdbcTemplate.update("INSERT INTO reservation VALUES (1, 'brown', '2024-11-15', 1)");
        //when
        Response deleteResponse = RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all().extract().response();
        //then
        Response reservationResponse = RestAssured.given().log().all()
                .when().get("reservations")
                .then().log().all().extract().response();
        assertAll(
                () -> assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(reservationResponse.jsonPath().getList(".")).isEmpty()
        );
    }

    @DisplayName("시간 생성에서 잘못된 값 입력시 예외메시지를 응답한다.")
    @Test
    void createException() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationTimeRequest(""))
                .when().post("/times")
                .then().log().all().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
