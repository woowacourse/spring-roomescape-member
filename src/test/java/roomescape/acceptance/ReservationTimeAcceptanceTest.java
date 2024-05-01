package roomescape.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.fixture.Fixture;
import roomescape.repository.ReservationTimeRepository;

class ReservationTimeAcceptanceTest extends BaseAcceptanceTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    private ReservationTime reservationTime1, reservationTime2;

    @BeforeEach
    void setUp() {
        reservationTime1 = reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        reservationTime2 = reservationTimeRepository.save(Fixture.RESERVATION_TIME_2);
    }

    @Test
    @DisplayName("모든 예약 시간들을 조회한다.")
    void getAllReservationTimes() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .extract();

        List<ReservationTimeResponse> reservationTimeResponses = getReservationTimeResponses(response);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(reservationTimeResponses).hasSize(2);
            softly.assertThat(reservationTimeResponses).containsExactly(
                    ReservationTimeResponse.from(reservationTime1),
                    ReservationTimeResponse.from(reservationTime2)
            );
        });
    }

    @Test
    @DisplayName("예약 시간을 조회한다.")
    void addReservationTime() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(makeReservationTimeRequest())
                .when().post("/times")
                .then().log().all()
                .extract();

        ReservationTimeResponse reservationTimeResponse = getReservationTimeResponse(response);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            softly.assertThat(response.header("Location"))
                    .isEqualTo("/times/" + reservationTimeResponse.id());
        });
    }

    @Test
    @DisplayName("예약 시간을 삭제한다.")
    void deleteReservationTime() {
        ReservationTime reservationTime = reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/times/" + reservationTime.getId())
                .then().log().all()
                .extract();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        });
    }

    private String makeReservationTimeRequest() {
        try {
            ReservationTimeRequest request = new ReservationTimeRequest(
                    LocalTime.of(15, 40)
            );
            return objectMapper.writeValueAsString(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ReservationTimeResponse getReservationTimeResponse(ExtractableResponse<Response> response) {
        return response.jsonPath().getObject(".", ReservationTimeResponse.class);
    }

    private List<ReservationTimeResponse> getReservationTimeResponses(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", ReservationTimeResponse.class);
    }
}
