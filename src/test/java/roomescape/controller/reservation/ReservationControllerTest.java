package roomescape.controller.reservation;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.dto.member.MemberResponse;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.dto.reservation.ReservationTimeResponse;
import roomescape.dto.reservation.ThemeResponse;
import roomescape.exceptions.EntityNotFoundException;
import roomescape.service.reservation.ReservationService;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @DisplayName("예약 목록을 조회한다.")
    void getAllReservations() {
        ReservationTimeResponse givenTime = new ReservationTimeResponse(1L, LocalTime.MAX);
        ThemeResponse givenTheme = new ThemeResponse(1L, "테스트", "테스트", "테스트");
        MemberResponse givenMember = new MemberResponse(1L, "테스트");
        ReservationResponse response1 = new ReservationResponse(1L,
                givenMember,
                LocalDate.now().plusDays(1),
                givenTime,
                givenTheme);
        ReservationResponse response2 = new ReservationResponse(2L,
                givenMember,
                LocalDate.now().plusDays(1),
                givenTime,
                givenTheme);

        List<ReservationResponse> reservations = List.of(
                response1, response2
        );

        given(reservationService.getReservations(any())).willReturn(reservations);

        RestAssuredMockMvc.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .status(HttpStatus.OK)
                .body("size()", is(2))
                .body("[0].id", is(1))
                .body("[0].member.name", is("테스트"))
                .body("[1].id", is(2))
                .body("[1].member.name", is("테스트"));
    }

    @Test
    @DisplayName("예약 관리 페이지 내에서 예약 추가한다.")
    void addReservation() {
        LocalDate fixedDate = LocalDate.of(2023, 5, 15);
        long expectedId = 1L;
        long expectedTimeId = 1L;
        long expectedThemeId = 1L;
        long expectedMemberId = 1L;
        ReservationRequest dto = new ReservationRequest(fixedDate, expectedTimeId, expectedThemeId, expectedMemberId);
        ReservationTimeResponse givenTime = new ReservationTimeResponse(expectedTimeId, LocalTime.MAX);
        ThemeResponse givenTheme = new ThemeResponse(expectedThemeId, "테스트", "테스트", "테스트");
        MemberResponse givenMember = new MemberResponse(expectedMemberId, "테스트");
        ReservationResponse response = new ReservationResponse(expectedId,
                givenMember,
                fixedDate,
                givenTime,
                givenTheme
        );
        given(reservationService.addReservation(dto)).willReturn(response);

        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(dto)
                .when().post("/reservations")
                .then().log().all()
                .status(HttpStatus.CREATED)
                .body("id", is((int) expectedId))
                .body("member.name", is("테스트"))
                .body("date", is(fixedDate.toString()))
                .body("time.id", is((int) expectedTimeId))
                .body("theme.id", is((int) expectedThemeId))
        ;
    }

    @Test
    @DisplayName("존재하는 ID로 삭제 요청 시 성공적으로 처리되어야 한다.")
    void deleteExistingReservation() {
        long reservationId = 1L;

        willDoNothing().given(reservationService).deleteReservation(reservationId);

        RestAssuredMockMvc.given().log().all()
                .when().delete("/reservations/" + reservationId)
                .then().log().all()
                .status(HttpStatus.NO_CONTENT);

        verify(reservationService, times(1)).deleteReservation(reservationId);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 삭제 요청 시 404 응답이 반환되어야 한다.")
    void deleteNonExistingReservation() {
        long nonExistingId = 999L;

        willThrow(new EntityNotFoundException("데이터를 찾을 수 없습니다."))
                .given(reservationService)
                .deleteReservation(nonExistingId);

        RestAssuredMockMvc.given().log().all()
                .when().delete("/reservations/" + nonExistingId)
                .then().log().all()
                .status(HttpStatus.NOT_FOUND);

        verify(reservationService, times(1)).deleteReservation(nonExistingId);
    }
}
