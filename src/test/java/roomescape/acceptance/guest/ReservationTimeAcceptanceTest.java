package roomescape.acceptance.guest;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dto.AvailableReservationTimeResponse;

import java.time.LocalDate;
import java.util.List;

import static roomescape.acceptance.PreInsertedData.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
class ReservationTimeAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    private void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("날짜와 테마에 해당하는 예약 시간 목록 조회")
    @Test
    void getAvailableTimes_success() {
        LocalDate date = preInsertedReservation.getDate();
        long themeId = preInsertedReservation.getTheme().getId();

        TypeRef<List<AvailableReservationTimeResponse>> availableTimesFormat = new TypeRef<>() {
        };
        List<AvailableReservationTimeResponse> availableReservationTimeResponses = RestAssured.given().log().all()
                .when().get("/times/available?date=" + date + "&themeId=" + themeId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(availableTimesFormat);

        Assertions.assertThat(availableReservationTimeResponses).containsExactlyInAnyOrder(
                AvailableReservationTimeResponse.from(preInsertedReservationTime1, false),
                AvailableReservationTimeResponse.from(preInsertedReservationTime2, true)
        );
    }
}
