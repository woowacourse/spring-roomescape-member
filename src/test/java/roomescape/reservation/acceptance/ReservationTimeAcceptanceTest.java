package roomescape.reservation.acceptance;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.dto.request.ReservationTimeRequest.ReservationTimeCreateRequest;
import roomescape.reservation.repository.ReservationTimeRepository;

// @formatter:off
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeAcceptanceTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    @DisplayName("예약 시간을 생성한다.")
    void createTime() throws Exception {
        // given
        var startAt = LocalTime.of(10, 0);
        var request = new ReservationTimeCreateRequest(startAt);

        // when & then
        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/times")
        .then()
                .statusCode(200)
                .body("startAt", equalTo(startAt.toString()));
    }

    @Test
    @DisplayName("운영 시간 이전의 예약 시간을 생성하면 예외가 발생한다.")
    void createTimeWithInvalidTime() throws Exception {
        // given
        var startAt = LocalTime.of(9, 0);
        var request = new ReservationTimeCreateRequest(startAt);

        // when & then
        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/times")
        .then()
                .statusCode(400)
                .body("message", equalTo("운영 시간은 10:00부터 22:00까지입니다."));
    }

    @Test
    @DisplayName("중복된 예약 시간을 생성하면 예외가 발생한다.")
    void createTimeWithDuplicateTime() throws Exception {
        // given
        var startAt = LocalTime.of(10, 0);
        var request = new ReservationTimeCreateRequest(startAt);

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/times")
        .then()
                .statusCode(200);

        // when & then
        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/times")
        .then()
                .statusCode(409)
                .body("message", equalTo("이미 존재하는 예약 시간입니다."));
    }

    @Test
    @DisplayName("모든 예약 시간을 조회한다.")
    void getAllTimes() throws Exception {
        // given
        var startAt = LocalTime.of(10, 0);
        var request = new ReservationTimeCreateRequest(startAt);

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/times")
        .then()
                .statusCode(200);

        // when & then
        given()
        .when()
                .get("/times")
        .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].startAt", equalTo(startAt.toString()));
    }

    @Test
    @DisplayName("예약 시간을 삭제한다.")
    void deleteTime() throws Exception {
        // given
        var startAt = LocalTime.of(10, 0);
        var request = new ReservationTimeCreateRequest(startAt);

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/times")
        .then()
                .statusCode(200);

        // when & then
        given()
        .when()
                .delete("/times/1")
        .then()
                .statusCode(200);

        given()
        .when()
                .get("/times")
        .then()
                .statusCode(200)
                .body("$", hasSize(0));
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간을 삭제하면 예외가 발생한다.")
    void deleteNonExistentTime() {
        // when & then
        given()
        .when()
                .delete("/times/1")
        .then()
                .statusCode(404)
                .body("message", equalTo("존재하지 않는 id 입니다."));
    }
}
