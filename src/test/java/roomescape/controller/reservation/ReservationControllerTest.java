package roomescape.controller.reservation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.controller.reservation.dto.MemberResponse;
import roomescape.controller.reservation.dto.ReservationResponse;
import roomescape.controller.theme.dto.ReservationThemeResponse;
import roomescape.controller.time.dto.AvailabilityTimeResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationControllerTest {

    @Autowired
    ReservationController reservationController;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("예약 조회")
    void getReservations() {
        final List<ReservationResponse> reservations = reservationController.getReservations();
        for (final ReservationResponse reservation : reservations) {
            System.out.println(reservation);
        }
        final LocalDate CURRENT_DATE = LocalDate.now();

        final List<ReservationResponse> expected = List.of(
                new ReservationResponse(1L, new MemberResponse("레디"), CURRENT_DATE.minusDays(3).toString(),
                        new AvailabilityTimeResponse(1L,
                                "15:00", false), new ReservationThemeResponse("봄")),
                new ReservationResponse(2L, new MemberResponse("재즈"), CURRENT_DATE.minusDays(2).toString(),
                        new AvailabilityTimeResponse(2L,
                                "17:00", false), new ReservationThemeResponse("여름")),
                new ReservationResponse(3L, new MemberResponse("레디"), CURRENT_DATE.minusDays(1).toString(),
                        new AvailabilityTimeResponse(3L,
                                "16:00", false), new ReservationThemeResponse("여름")),
                new ReservationResponse(4L, new MemberResponse("재즈"), CURRENT_DATE.minusDays(1).toString(),
                        new AvailabilityTimeResponse(4L,
                                "15:00", false), new ReservationThemeResponse("여름")),
                new ReservationResponse(5L, new MemberResponse("제제"), CURRENT_DATE.minusDays(7).toString(),
                        new AvailabilityTimeResponse(5L,
                                "15:00", false), new ReservationThemeResponse("가을")),
                new ReservationResponse(6L, new MemberResponse("제제"), CURRENT_DATE.plusDays(3).toString(),
                        new AvailabilityTimeResponse(6L,
                                "18:00", false), new ReservationThemeResponse("가을"))
        );
        assertThat(reservations).isEqualTo(expected);
    }

    @Test
    @DisplayName("api로 예약 조회")
    void getReservationsApi() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(6));
    }

    @ParameterizedTest
    @MethodSource("invalidRequestParameterProvider")
    @DisplayName("유효하지 않는 요청인 경우 400을 반환한다.")
    void invalidRequest(final String name, final String date, final String timeId, final String themeId) {
        final Map<String, String> params = Map.of("name", name, "date", date, "timeId", timeId, "themeId", themeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    static Stream<Arguments> invalidRequestParameterProvider() {
        final String name = "name";
        final String date = LocalDate.now().plusDays(5).format(DateTimeFormatter.ISO_DATE);
        final String timeId = "1";
        final String themeId = "1";

        return Stream.of(
                Arguments.of(name, date, "dk", themeId),
                Arguments.of(name, date, timeId, "al"),
                Arguments.of(name, "2023", timeId, themeId),
                Arguments.of("  ", date, timeId, themeId)
        );
    }
}
