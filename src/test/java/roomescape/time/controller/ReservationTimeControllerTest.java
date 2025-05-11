package roomescape.time.controller;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.global.exception.EntityNotFoundException;
import roomescape.global.security.jwt.JwtTokenExtractor;
import roomescape.global.security.jwt.JwtTokenProvider;
import roomescape.member.service.MemberService;
import roomescape.time.dto.ReservationTimeRequest;
import roomescape.time.dto.ReservationTimeResponse;
import roomescape.time.service.ReservationTimeService;

import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WebMvcTest(ReservationTimeController.class)
public class ReservationTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationTimeService timeService;

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private JwtTokenExtractor jwtTokenExtractor;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @DisplayName("예약 시간 목록을 조회한다.")
    void getReservationTimes() {
        ReservationTimeResponse response1 = new ReservationTimeResponse(1L, LocalTime.of(11, 0));
        ReservationTimeResponse response2 = new ReservationTimeResponse(2L, LocalTime.of(12, 0));

        List<ReservationTimeResponse> reservationTimeResponses = List.of(
                response1, response2
        );

        given(timeService.readReservationTimes()).willReturn(reservationTimeResponses);

        RestAssuredMockMvc.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2))
                .body("[0].id", is(1))
                .body("[0].startAt", is("11:00"))
                .body("[1].id", is(2))
                .body("[1].startAt", is("12:00"));
    }

    @Test
    @DisplayName("예약 시간 관리 페이지 내에서 예악 시간 추가한다.")
    void postReservationTime() {
        long expectedId = 1L;
        LocalTime time = LocalTime.of(11, 0);

        ReservationTimeRequest request = new ReservationTimeRequest(time);

        ReservationTimeResponse response = new ReservationTimeResponse(expectedId, time);
        given(timeService.createReservationTime(request)).willReturn(response);

        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .body("id", is((int) expectedId))
                .body("startAt", is(time.toString()));
    }

    @Test
    @DisplayName("존재하는 ID로 삭제 요청 시 성공적으로 처리되어야 한다.")
    void deleteExistingTime() {
        long timeId = 1L;

        willDoNothing().given(timeService).deleteReservationTime(timeId);

        RestAssuredMockMvc.given().log().all()
                .when().delete("/times/" + timeId)
                .then().log().all()
                .statusCode(204);

        verify(timeService, times(1)).deleteReservationTime(timeId);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 삭제 요청 시 404 응답이 반환되어야 한다.")
    void deleteReservationTime() {
        long nonExistingId = 999L;

        willThrow(new EntityNotFoundException("데이터를 찾을 수 없습니다."))
                .given(timeService)
                .deleteReservationTime(nonExistingId);

        RestAssuredMockMvc.given().log().all()
                .when().delete("/times/" + nonExistingId)
                .then().log().all()
                .statusCode(404);

        verify(timeService, times(1)).deleteReservationTime(nonExistingId);
    }
}
