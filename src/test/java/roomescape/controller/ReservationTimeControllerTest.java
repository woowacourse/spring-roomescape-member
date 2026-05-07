package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import roomescape.domain.ReservationTime;
import roomescape.dto.ResourceIdResponse;
import roomescape.dto.reservationTime.AvailableReservationTimesResponse;
import roomescape.dto.reservationTime.ReservationTimeRequest;
import roomescape.dto.reservationTime.ReservationTimeResponse;
import roomescape.service.ReservationService;
import roomescape.service.ThemeService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationTimeControllerTest {

    @LocalServerPort
    private int port;

    @MockitoBean
    private ReservationService reservationService;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @Test
    void 전체_예약_시간을_조회한다() {
        // given
        List<ReservationTime> times = List.of(
            new ReservationTime(1L, "12:30"),
            new ReservationTime(2L, "14:30"));

        List<ReservationTimeResponse> expectedResponse = times.stream()
            .map(ReservationTimeResponse::from)
            .toList();

        when(reservationService.getReservationTimes())
            .thenReturn(times);

        // when
        Response response = RestAssured
            .given().log().all()
            .when().get("/times");

        // then
        response
            .then()
            .statusCode(HttpStatus.OK.value());

        List<ReservationTimeResponse> actualResponse = response.as(new TypeRef<>() {
        });
        assertThat(actualResponse).containsExactlyElementsOf(expectedResponse);
    }

    @Test
    void 관리자는_예약_시간을_추가할_수_있다() {
        // given
        ReservationTime newTime = reservationTime();
        ReservationTimeRequest request = requestDtoFrom(newTime);
        when(reservationService.addReservationTime(any()))
            .thenReturn(newTime);

        // when
        Response response = RestAssured
            .given().log().all()
            .queryParam("role", "admin")
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/times");

        // then
        response
            .then()
            .statusCode(HttpStatus.CREATED.value());

        ResourceIdResponse responseDto = response.as(ResourceIdResponse.class);
        assertThat(responseDto).isEqualTo(new ResourceIdResponse(newTime.getId()));
    }

    @Test
    void 관리자는_예약_시간을_삭제할_수_있다() {
        // given & when
        Response response = RestAssured
            .given().log().all()
            .queryParam("role", "admin")
            .pathParam("id", 1)
            .when().delete("/times/{id}");

        // then
        response
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 날짜와_테마아이디로_예약가능한_시간을_조회한다() {
        // given
        ReservationTime availableTime = reservationTime();
        ReservationTime impossibleTime = new ReservationTime(2L, "14:30");
        List<ReservationTime> allTimes = List.of(availableTime, impossibleTime);
        List<ReservationTime> availableTimes = List.of(availableTime);
        AvailableReservationTimesResponse expected = AvailableReservationTimesResponse.of(
            allTimes, availableTimes);

        when(reservationService.getAvailableTimes(any(), anyLong()))
            .thenReturn(List.of(availableTime));

        when(reservationService.getReservationTimes())
            .thenReturn(List.of(availableTime, impossibleTime));

        // when
        Response response = RestAssured
            .given().log().all()
            .queryParam("date", "2026-05-05")
            .queryParam("themeId", 1)
            .when().get("/times/available");

        // then
        response
            .then()
            .statusCode(HttpStatus.OK.value());

        AvailableReservationTimesResponse responseDto = response.as(AvailableReservationTimesResponse.class);
        assertThat(responseDto).isEqualTo(expected);
    }

    @Nested
    @DisplayName("인가 권한이 없는 경우 예외가 발생한다")
    class RoleForbidden {
        @Test
        void 관리자가_아닌_사용자가_시간을_추가하는_경우_예외가_발생한다() {
            // given
            ReservationTime newTime = reservationTime();
            ReservationTimeRequest request = requestDtoFrom(newTime);

            // when
            Response response = RestAssured
                .given().log().all()
                .queryParam("role", "user")
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times");

            // then
            response
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
        }

        @Test
        void 관리자가_아닌_사용자가_테마를_삭제하는_경우_예외가_발생한다() {
            // given & when
            Response response = RestAssured
                .given().log().all()
                .queryParam("role", "user")
                .pathParam("id", 1)
                .when().delete("/times/{id}");

            // then
            response
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
        }
    }

    private ReservationTime reservationTime() {
        return new ReservationTime(1L, "12:30");
    }

    private ReservationTimeRequest requestDtoFrom(ReservationTime time) {
        return new ReservationTimeRequest(time.getStartAt());
    }
}