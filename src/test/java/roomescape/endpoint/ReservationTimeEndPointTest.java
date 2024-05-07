package roomescape.endpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.dto.AvailableReservationTimeResponse;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
public class ReservationTimeEndPointTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void cleanUp() {
        jdbcTemplate.update("DELETE FROM reservation");
    }

    @DisplayName("예약 시간 목록을 조회하면 상태 코드 200과 예약 시간 목록을 응답으로 반환한다.")
    @Test
    void getReservationTimes() {
        List<ReservationTimeResponse> responses = RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList(".", ReservationTimeResponse.class);

        List<ReservationTimeResponse> expected = List.of(
                new ReservationTimeResponse(1L, LocalTime.parse("10:00:00")),
                new ReservationTimeResponse(2L, LocalTime.parse("11:00:00"))
        );

        assertThat(responses)
                .isEqualTo(expected);
    }

    @DisplayName("예약 시간을 추가하면 상태 코드 201와 추가된 객체를 반환한다.")
    @Test
    void addReservationTime() {
        LocalTime time = LocalTime.now().plusHours(1);
        ReservationTimeRequest request = new ReservationTimeRequest(time);
        ReservationTimeResponse expected = new ReservationTimeResponse(3L, time);

        ReservationTimeResponse response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(ReservationTimeResponse.class);

        assertThat(response)
                .isEqualTo(expected);
    }

    @DisplayName("예약 시간을 삭제하면 상태 코드 204를 반환한다.")
    @Test
    void deleteReservationTime() {
        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .body(notNullValue());
    }

    @DisplayName("예약이 존재하는 시간을 삭제하면 상태 코드 400을 반환한다.")
    @Test
    void deleteReservationTimeFailed() {
        RestAssured.given().log().all()
                .when().delete("/times/2")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("예약 가능 시간을 조회하면 상태코드 200과 예약 가능 여부를 담은 시간 목록을 반환한다.")
    @Test
    void getAvailableTimes() {
        List<AvailableReservationTimeResponse> responses = RestAssured.given().log().all()
                .when().get("/times/available?date=2024-05-01&themeId=2")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList(".", AvailableReservationTimeResponse.class);

        List<AvailableReservationTimeResponse> expected = List.of(
                new AvailableReservationTimeResponse(1L, LocalTime.parse("10:00:00"), false),
                new AvailableReservationTimeResponse(2L, LocalTime.parse("11:00:00"), true)
        );

        assertThat(responses)
                .isEqualTo(expected);
    }
}
