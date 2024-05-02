package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;

class ControllerTest extends BaseControllerTest {

    @TestFactory
    @DisplayName("예약을 생성한다.")
    Stream<DynamicTest> acceptanceTests() {
        return Stream.of(
                DynamicTest.dynamicTest("예약 시간을 생성한다.", this::addReservationTime),
                DynamicTest.dynamicTest("예약 시간을 조회한다.", this::findAllReservationTimes),
                DynamicTest.dynamicTest("테마를 생성한다.", this::addTheme)
        );
    }

    void addReservationTime() {
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(10, 30));

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all()
                .extract();

        ReservationTimeResponse reservationTimeResponse = response.as(ReservationTimeResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isEqualTo("/times/" + reservationTimeResponse.id())
        );
    }

    private void findAllReservationTimes() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .extract();

        List<ReservationTimeResponse> reservationTimeResponses = response.jsonPath()
                .getList(".", ReservationTimeResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        );
    }

    void addTheme() {
        ThemeRequest request = new ThemeRequest("테스트", "설명", "이미지");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .then().log().all()
                .extract();

        ThemeResponse themeResponse = response.as(ThemeResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isEqualTo("/themes/" + themeResponse.id())
        );
    }
}
