package roomescape.endpoint;

import java.util.List;
import static org.hamcrest.Matchers.notNullValue;
import static roomescape.endpoint.HttpRestTestTemplate.*;
import static roomescape.endpoint.RequestFixture.*;
import static roomescape.endpoint.RequestFixture.themeId;
import static roomescape.endpoint.ResponseFormatTestTemplate.*;
import static roomescape.endpoint.ResponseFormatTestTemplate.assertBodyFormat;
import static roomescape.endpoint.ResponseFormatTestTemplate.assertHeaderContainsValue;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.controller.exception.CustomExceptionResponse;
import roomescape.dto.AvailableReservationTimeResponse;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationTimeResponse;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
class ReservationTimeEndPointTest {

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("[예약 시간 목록 조회] 정상 작동")
    @Test
    void getReservationTimes() {
        assertGetOk("/times");
    }

    @DisplayName("[예약 시간 추가] 정상 작동")
    @Test
    void addReservationTime() {
        Response response = assertPostCreated(reservationTimeRequest, "/times");
        assertHeaderContainsValue(response, "location", "/times/");
        assertBodyFormat(objectMapper, response, ReservationTimeResponse.class);
    }

    @DisplayName("[예약 시간 추가, 해당 예약 시간 삭제] 정상 작동")
    @Test
    void deleteReservationTime() {
        Response response = assertPostCreated(reservationTimeRequest, "/times");
        String createdId = response.getBody().path("id").toString();

        assertDeleteNoContent("/times/" + createdId);
    }

    @DisplayName("[예약 시간 추가, 해당 시간에 예약 추가, 예약 시간 삭제] 예외 발생")
    @Test
    void deleteReservationTimeFailed() {
        Response response = assertPostCreated(reservationTimeRequest, "/times");
        String createdId = response.getBody().path("id").toString();
        ReservationRequest reservationRequest = new ReservationRequest(
                "산초",
                LocalDate.now().plusDays(1),
                Long.parseLong(createdId),
                themeId
        );
        assertPostCreated(reservationRequest, "/reservations");

        Response errorResponse = assertDeleteBadRequest("/times/" + createdId);
        assertBodyFormat(objectMapper, errorResponse, CustomExceptionResponse.class);
    }

    @DisplayName("[예약 시간 추가, 테마 추가, 예약 추가, 날짜와 테마에 해당하는 예약 조회] 정상 작동")
    @Test
    void getAvailableTimes() {
        Response timeResponse = assertPostCreated(reservationTimeRequest, "/times");
        String timeId = timeResponse.getBody().path("id").toString();
        Response themeResponse = assertPostCreated(themeRequest, "/themes");
        String themeId = themeResponse.getBody().path("id").toString();
        ReservationRequest reservationRequest = new ReservationRequest(
                "산초",
                LocalDate.parse("2099-01-11"),
                Long.parseLong(timeId),
                Long.parseLong(themeId)
        );
        assertPostCreated(reservationRequest, "/reservations");

        Response response = assertGetOk("/times/available?date=2099-01-11&themeId=" + themeId);
        assertBodyListFormat(objectMapper, response, AvailableReservationTimeResponse.class);
    }
}
