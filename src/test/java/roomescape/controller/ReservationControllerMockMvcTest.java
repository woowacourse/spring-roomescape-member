package roomescape.controller;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.exceptions.EntityNotFoundException;
import roomescape.service.ReservationService;

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
public class ReservationControllerMockMvcTest {

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
    void readReservation() {
        ReservationTimeResponse givenTime = new ReservationTimeResponse(1L, LocalTime.MAX);
        ReservationResponse response1 = new ReservationResponse(1L, "브라운", LocalDate.now().plusDays(1), givenTime);
        ReservationResponse response2 = new ReservationResponse(2L, "네오", LocalDate.now().plusDays(1), givenTime);

        List<ReservationResponse> reservations = List.of(
                response1, response2
        );

        given(reservationService.readReservation()).willReturn(reservations);

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
    void postReservation() {
        LocalDate fixedDate = LocalDate.of(2023, 5, 15);
        long expectedTimeId = 1L;
        long expectedId = 1L;

        ReservationRequest dto = new ReservationRequest("브라운", fixedDate, expectedTimeId);
        ReservationTimeResponse givenTime = new ReservationTimeResponse(expectedTimeId, LocalTime.MAX);

        ReservationResponse response = new ReservationResponse(expectedId, "브라운", fixedDate, givenTime);
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
                .body("time.id", is((int) expectedTimeId));
    }

    @Test
    @DisplayName("존재하는 ID로 삭제 요청 시 성공적으로 처리되어야 한다.")
    void deleteExistingReservation() {
        long reservationId = 1L;

        willDoNothing().given(reservationService).deleteReservation(reservationId);

        RestAssuredMockMvc.given().log().all()
                .when().delete("/reservations/" + reservationId)
                .then().log().all()
                .statusCode(204);

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
                .statusCode(404);

        verify(reservationService, times(1)).deleteReservation(nonExistingId);
    }
}
