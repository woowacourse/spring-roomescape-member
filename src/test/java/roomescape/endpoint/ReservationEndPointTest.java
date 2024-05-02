package roomescape.endpoint;

import static org.hamcrest.Matchers.notNullValue;
import static roomescape.endpoint.RequestFixture.reservationRequest;
import static roomescape.endpoint.RequestFixture.reservationTimeId;
import static roomescape.endpoint.RequestFixture.themeId;

import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.dto.ReservationRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ReservationEndPointTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void cleanUp() {
        jdbcTemplate.update("DELETE FROM reservation");
    }

    @DisplayName("예약 목록 조회")
    @Test
    void getReservations() {
        HttpRestTestTemplate.assertGetOk("/reservations");
    }

    @DisplayName("예약 추가")
    @Test
    void addReservation() {
        HttpRestTestTemplate.assertPostCreated(reservationRequest, "/reservations", "id", notNullValue());
    }

    @DisplayName("예약 삭제")
    @Test
    void deleteReservation() {
        HttpRestTestTemplate.assertDeleteNoContent("/reservations/1");
    }

    @DisplayName("예약 추가 불가능 - 과거 시간 예약")
    @Test
    void validateReservationTimeIsFutureFail() {
        ReservationRequest past = new ReservationRequest(
                "알파카",
                LocalDate.now().minusDays(1),
                reservationTimeId,
                themeId
        );

        HttpRestTestTemplate.assertPostBadRequest(past, "/reservations");
    }

    @DisplayName("예약 추가 불가능 - 중복 예약")
    @TestFactory
    void validateReservationIsDuplicatedFail() {
        ReservationRequest duplicated = new ReservationRequest(
                "알파카",
                LocalDate.now().plusDays(1),
                reservationTimeId,
                themeId
        );

        HttpRestTestTemplate.assertPostCreated(duplicated, "/reservations", "id", notNullValue());
        HttpRestTestTemplate.assertPostBadRequest(duplicated, "/reservations");
    }
}
