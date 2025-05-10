package roomescape.reservation.controller;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.common.security.jwt.JwtTokenProvider;
import roomescape.member.service.MemberService;
import roomescape.reservation.dto.ReservationAvailableTimeResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.service.ReservationService;
import roomescape.theme.dto.ThemeResponse;
import roomescape.time.dto.ReservationTimeResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @DisplayName("예약 목록을 조회한다.")
    void getReservations() {
        ReservationTimeResponse givenTime = new ReservationTimeResponse(1L, LocalTime.MAX);
        ThemeResponse givenTheme = new ThemeResponse(1L, "테스트", "테스트", "테스트");
        ReservationResponse response1 = new ReservationResponse(1L, "브라운", LocalDate.now().plusDays(1), givenTime,
                givenTheme);
        ReservationResponse response2 = new ReservationResponse(2L, "네오", LocalDate.now().plusDays(1), givenTime,
                givenTheme);

        List<ReservationResponse> reservations = List.of(
                response1, response2
        );

        given(reservationService.readReservations()).willReturn(reservations);

        RestAssuredMockMvc.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2))
                .body("[0].id", is(1))
                .body("[0].name", is("브라운"))
                .body("[1].id", is(2))
                .body("[1].name", is("네오"));
    }

    @Test
    @DisplayName("예약 관리 페이지 내에서 예약 추가한다.")
    void postReservationInAdminPage() {
        LocalDate fixedDate = LocalDate.of(2023, 5, 15);
        long expectedId = 1L;
        long expectedTimeId = 1L;
        long expectedThemeId = 1L;

        ReservationRequest dto = new ReservationRequest("브라운", fixedDate, expectedTimeId, expectedThemeId);
        ReservationTimeResponse givenTime = new ReservationTimeResponse(expectedTimeId, LocalTime.MAX);

        ThemeResponse givenTheme = new ThemeResponse(1L, "테스트", "테스트", "테스트");
        ReservationResponse response = new ReservationResponse(expectedId, "브라운", fixedDate, givenTime, givenTheme);
        given(reservationService.createReservation(dto)).willReturn(response);

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
    @DisplayName("존재하는 ID로 삭제 요청 시 성공적으로 처리되어야 한다.")
    void deleteExistingReservation() {
        long reservationId = 1L;

        willDoNothing().given(reservationService).deleteReservationById(reservationId);

        RestAssuredMockMvc.given().log().all()
                .when().delete("/reservations/" + reservationId)
                .then().log().all()
                .statusCode(204);

        verify(reservationService, times(1)).deleteReservationById(reservationId);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 삭제 요청 시 404 응답이 반환되어야 한다.")
    void deleteNonExistingReservation() {
        long nonExistingId = 999L;

        willThrow(new EntityNotFoundException("데이터를 찾을 수 없습니다."))
                .given(reservationService)
                .deleteReservationById(nonExistingId);

        RestAssuredMockMvc.given().log().all()
                .when().delete("/reservations/" + nonExistingId)
                .then().log().all()
                .statusCode(404);

        verify(reservationService, times(1)).deleteReservationById(nonExistingId);
    }

    @Test
    @DisplayName("사용자가 예약을 추가한다.")
    void postReservationInUserPage() {
        LocalDate fixedDate = LocalDate.of(2023, 5, 15);
        long expectedId = 1L;
        long expectedTimeId = 1L;
        long expectedThemeId = 1L;

        ReservationRequest request = new ReservationRequest("브라운", fixedDate, expectedTimeId, expectedThemeId);
        ReservationTimeResponse givenTime = new ReservationTimeResponse(expectedTimeId, LocalTime.MAX);

        ThemeResponse givenTheme = new ThemeResponse(1L, "테스트", "테스트", "테스트");
        ReservationResponse response = new ReservationResponse(expectedId, "브라운", fixedDate, givenTime, givenTheme);
        given(reservationService.createReservation(request)).willReturn(response);

        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
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
    void getAvailableReservationTimes() {
        ReservationAvailableTimeResponse response1 = new ReservationAvailableTimeResponse(1L, LocalTime.of(10, 0),
                false);
        ReservationAvailableTimeResponse response2 = new ReservationAvailableTimeResponse(2L, LocalTime.of(11, 0),
                true);

        List<ReservationAvailableTimeResponse> reservationAvailableTimeResponses = List.of(
                response1, response2
        );

        LocalDate date = LocalDate.of(2025, 1, 1);
        long themeId = 1L;
        given(reservationService.readAvailableReservationTimes(date, themeId)).willReturn(
                reservationAvailableTimeResponses);

        RestAssuredMockMvc.given().log().all()
                .when().get("/reservations/times?date=" + date + "&themeId=" + (int) themeId)
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2))
                .body("[0].id", is(1))
                .body("[0].isBooked", is(false))
                .body("[1].id", is(2))
                .body("[1].isBooked", is(true));
    }
}
