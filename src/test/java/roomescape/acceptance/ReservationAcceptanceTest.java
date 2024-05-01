package roomescape.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.fixture.Fixture;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

class ReservationAcceptanceTest extends BaseAcceptanceTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private ReservationTime reservationTime;
    private Reservation reservation1, reservation2;

    @BeforeEach
    void setUp() {
        reservationTime = reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        reservation1 = reservationRepository.save(Fixture.getReservation(reservationTime));
        reservation2 = reservationRepository.save(Fixture.getReservation(reservationTime));
    }

    @Test
    @DisplayName("모든 예약들을 조회한다.")
    void getAllReservations() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .extract();

        List<ReservationResponse> reservationResponses = getReservationResponses(response);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(reservationResponses).hasSize(2);
            softly.assertThat(reservationResponses).containsExactly(
                    ReservationResponse.from(reservation1),
                    ReservationResponse.from(reservation2)
            );
        });
    }

    @Test
    @DisplayName("예약을 추가한다.")
    void addReservation() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(makeReservationRequest(reservationTime.getId()))
                .when().post("/reservations")
                .then().log().all()
                .extract();

        ReservationResponse reservationResponse = getReservationResponse(response);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            softly.assertThat(response.header("Location"))
                    .isEqualTo("/reservations/" + reservationResponse.id());
        });
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void deleteReservation() {
        ReservationTime savedReservationTime = reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/reservations/" + savedReservationTime.getId())
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private String makeReservationRequest(Long timeId) {
        try {
            ReservationRequest request = new ReservationRequest(
                    "구름",
                    LocalDate.of(2024, 4, 22),
                    timeId
            );
            return objectMapper.writeValueAsString(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ReservationResponse getReservationResponse(ExtractableResponse<Response> response) {
        return response.jsonPath().getObject(".", ReservationResponse.class);
    }

    private List<ReservationResponse> getReservationResponses(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", ReservationResponse.class);
    }
}
