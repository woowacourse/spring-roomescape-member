package roomescape.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.dto.ReservationAvailableTimeResponse;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ThemeResponse;
import roomescape.service.ReservationService;
import roomescape.service.UserReservationTimeService;

@WebMvcTest(UserReservationController.class)
class UserReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;
    @MockitoBean
    private UserReservationTimeService userReservationTimeService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @DisplayName("사용자가 예약을 추가한다.")
    void postReservation() {
        LocalDate fixedDate = LocalDate.of(2023, 5, 15);
        long expectedId = 1L;
        long expectedTimeId = 1L;
        long expectedThemeId = 1L;

        ReservationRequest dto = new ReservationRequest("브라운", fixedDate, expectedTimeId, expectedThemeId);
        ReservationTimeResponse givenTime = new ReservationTimeResponse(expectedTimeId, LocalTime.MAX);

        ThemeResponse givenTheme = new ThemeResponse(1L, "테스트", "테스트", "테스트");
        ReservationResponse response = new ReservationResponse(expectedId, "브라운", fixedDate, givenTime, givenTheme);
        given(reservationService.postReservation(dto)).willReturn(response);

        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(dto)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is((int) expectedId))
                .body("name", is("브라운"))
                .body("date", is(fixedDate.toString()))
                .body("time.id", is((int) expectedTimeId))
                .body("theme.id", is((int) expectedThemeId))
        ;
    }

    @Test
    @DisplayName("사용자가 예약 가능한 시간을 조회한다.")
    void readAvailableReservationTimes() {
        ReservationAvailableTimeResponse response1 = new ReservationAvailableTimeResponse(1L, LocalTime.of(10, 0),
                false);
        ReservationAvailableTimeResponse response2 = new ReservationAvailableTimeResponse(2L, LocalTime.of(11, 0),
                true);

        List<ReservationAvailableTimeResponse> reservationAvailableTimeResponses = List.of(
                response1, response2
        );

        LocalDate date = LocalDate.of(2025, 1, 1);
        long themeId = 1L;
        given(userReservationTimeService.readAvailableReservationTimes(date, themeId)).willReturn(
                reservationAvailableTimeResponses);

        RestAssuredMockMvc.given().log().all()
                .when().get("/user/times?date=" + date + "&themeId=" + (int) themeId)
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2))
                .body("[0].id", is(1))
                .body("[0].isBooked", is(false))
                .body("[1].id", is(2))
                .body("[1].isBooked", is(true));
    }
}
