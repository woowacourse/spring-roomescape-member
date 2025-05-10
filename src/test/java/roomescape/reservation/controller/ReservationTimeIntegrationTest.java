package roomescape.reservation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.controller.dto.ReservationTimeResponse;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.repository.ReservationTimeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeIntegrationTest {

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @DisplayName("예약 시간 목록 조회 시 DB에 저장된 예약 시간 목록을 반환한다")
    @Test
    void get_times_test() {
        // when
        List<ReservationTimeResponse> times = RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationTimeResponse.class);

        // then
        List<ReservationTime> savedTimes = reservationTimeRepository.findAll();
        assertAll(
                () -> assertThat(times).hasSize(savedTimes.size()),
                () -> assertThat(times).extracting(ReservationTimeResponse::id)
                        .containsExactlyInAnyOrderElementsOf(
                                savedTimes.stream()
                                        .map(ReservationTime::getId)
                                        .toList()
                        ),
                () -> assertThat(times).extracting(ReservationTimeResponse::startAt)
                        .containsExactlyInAnyOrderElementsOf(
                                savedTimes.stream()
                                        .map(ReservationTime::getStartAt)
                                        .toList()
                        )
        );
    }

    @DisplayName("예약 시간를 생성하면 DB에 예약 시간 데이터가 저장된다")
    @Test
    void add_time_test() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:20");

        // when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .body("id", is(7));

        // then
        ReservationTime savedTime = reservationTimeRepository.findById(7L).get();

        assertAll(
                () -> assertThat(savedTime.getId()).isEqualTo(7L),
                () -> assertThat(savedTime.getStartAt()).isEqualTo(LocalTime.of(10, 20))
        );
    }

    @DisplayName("예약 시간을 삭제하면 DB의 예약 시간 데이터가 삭제된다")
    @Test
    void delete_time_test() {
        // when
        RestAssured.given().log().all()
                .when().delete("/times/6")
                .then().log().all()
                .statusCode(204);

        // then
        assertAll(
                () -> assertThat(reservationTimeRepository.findAll()).hasSize(5),
                () -> assertThat(reservationTimeRepository.findById(6L).isEmpty()).isTrue()
        );
    }

    @DisplayName("예약 시간 생성 시 입력 값이 존재하지 않거나 공백이면 예외가 발생한다")
    @EmptySource
    @ValueSource(strings = {"", "\t", "\r"})
    @ParameterizedTest
    void add_time_null_empty_exception(String startAt) {
        // given
        Map<String, String> requestBody = Map.of("startAt", startAt);

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/times")
                .then().log().all()
                .statusCode(400)
                .body(equalTo("요청 형식이 올바르지 않습니다."));
    }

    @DisplayName("테마 삭제 시 연관된 예약 데이터가 존재하여 예외가 발생한다.")
    @Test
    void delete_time_exception() {
        // when & then
        RestAssured.given().log().all()
                .when().delete("/times/3")
                .then().log().all()
                .statusCode(400)
                .body(equalTo("해당 시간과 연관된 예약이 있어 삭제할 수 없습니다."));
    }
}
