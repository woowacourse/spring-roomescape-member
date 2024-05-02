package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ThemeResponse;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

class ReservationControllerTest extends BaseControllerTest {

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        reservationTimeRepository.save(new ReservationTime(LocalTime.of(11, 0)));
        themeRepository.save(new Theme("테마 이름", "테마 설명", "https://example.com"));
    }

    @TestFactory
    @DisplayName("예약을 생성, 조회, 삭제한다.")
    Stream<DynamicTest> reservationControllerTests() {
        return Stream.of(
                DynamicTest.dynamicTest("예약을 생성한다.", this::addReservation),
                DynamicTest.dynamicTest("예약을 모두 조회한다.", this::getAllReservations),
                DynamicTest.dynamicTest("예약을 삭제한다.", this::deleteReservationById)
        );
    }

    void addReservation() {
        ReservationRequest request = new ReservationRequest("구름", LocalDate.of(2024, 4, 9), 1L, 1L);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        ReservationResponse reservationResponse = response.as(ReservationResponse.class);
        ReservationTimeResponse reservationTimeResponse = new ReservationTimeResponse(1L, LocalTime.of(11, 0));
        ThemeResponse themeResponse = new ThemeResponse(1L, "테마 이름", "테마 설명", "https://example.com");

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            softly.assertThat(response.header("Location")).isEqualTo("/reservations/1");
            softly.assertThat(reservationResponse).isEqualTo(
                    new ReservationResponse(1L, "구름", LocalDate.of(2024, 4, 9), reservationTimeResponse,
                            themeResponse));
        });
    }

    void getAllReservations() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .extract();

        List<ReservationResponse> reservationResponses = response.jsonPath()
                .getList(".", ReservationResponse.class);
        ReservationTimeResponse reservationTimeResponse = new ReservationTimeResponse(1L, LocalTime.of(11, 0));
        ThemeResponse themeResponse = new ThemeResponse(1L, "테마 이름", "테마 설명", "https://example.com");

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(reservationResponses).hasSize(1);
            softly.assertThat(reservationResponses)
                    .containsExactly(
                            new ReservationResponse(1L, "구름", LocalDate.of(2024, 4, 9), reservationTimeResponse,
                                    themeResponse));
        });
    }

    void deleteReservationById() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .extract();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        });
    }
}
