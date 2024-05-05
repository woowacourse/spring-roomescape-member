package roomescape.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.controller.exception.CustomExceptionResponse;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;

import java.time.LocalDate;

import static roomescape.endpoint.HttpRestTestTemplate.*;
import static roomescape.endpoint.RequestFixture.*;
import static roomescape.endpoint.ResponseFormatTestTemplate.assertBodyFormat;
import static roomescape.endpoint.ResponseFormatTestTemplate.assertHeaderContainsValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
class ReservationEndPointTest {

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("예약 목록 조회")
    @Test
    void getReservations() {
        assertGetOk("/reservations");
    }

    @DisplayName("[예약 추가] 정상 작동")
    @Test
    void addReservation() {
        Response response = assertPostCreated(reservationRequest, "/reservations");
        assertHeaderContainsValue(response, "location", "/reservations/");
        assertBodyFormat(objectMapper, response, ReservationResponse.class);
    }

    @DisplayName("[예약 추가, 해당 예약 삭제] 정상 작동")
    @Test
    void deleteReservation() {
        Response response = assertPostCreated(reservationRequest, "/reservations");
        String createdId = response.getBody().path("id").toString();

        assertDeleteNoContent("/reservations/" + createdId);
    }

    @DisplayName("[과거 예약 추가] 예외 발생")
    @Test
    void validateReservationTimeIsFutureFail() {
        ReservationRequest past = new ReservationRequest(
                "알파카",
                LocalDate.now().minusDays(1),
                reservationTimeId,
                themeId
        );

        Response response = assertPostBadRequest(past, "/reservations");
        assertBodyFormat(objectMapper, response, CustomExceptionResponse.class);
    }

    @DisplayName("[예약 추가, 동일한 예약 추가] 예외 발생")
    @TestFactory
    void validateReservationIsDuplicatedFail() {
        ReservationRequest duplicated = new ReservationRequest(
                "알파카",
                LocalDate.now().plusDays(1),
                reservationTimeId,
                themeId
        );

        assertPostCreated(duplicated, "/reservations");
        Response response = assertPostBadRequest(duplicated, "/reservations");
        assertBodyFormat(objectMapper, response, CustomExceptionResponse.class);
    }
}
