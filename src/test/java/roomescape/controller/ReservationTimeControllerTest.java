package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import roomescape.domain.ReservationTime;
import roomescape.dto.reservationTime.ReservationTimeResponseDto;
import roomescape.service.ReservationService;

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
    void 날짜와_테마아이디로_예약가능한_시간을_조회한다() {
        ReservationTime availableTime1 = new ReservationTime(1L, "12:30");
        ReservationTime availableTime2 = new ReservationTime(2L, "14:30");
        // given
        when(reservationService.getAvailableTimes(any(), anyLong()))
            .thenReturn(List.of(availableTime1, availableTime2));

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

        List<ReservationTimeResponseDto> responseDtos = response.as(new TypeRef<>() {
        });
        assertThat(responseDtos).hasSize(2);
        assertThat(responseDtos).containsExactlyElementsOf(responseDtos);
    }
}