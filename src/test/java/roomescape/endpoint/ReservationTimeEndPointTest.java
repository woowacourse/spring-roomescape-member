package roomescape.endpoint;

import static org.hamcrest.Matchers.notNullValue;
import static roomescape.endpoint.RequestFixture.reservationTimeRequest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
public class ReservationTimeEndPointTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void cleanUp() {
        jdbcTemplate.update("DELETE FROM reservation");
    }

    @DisplayName("예약 시간 목록 조회")
    @Test
    void getReservationTimes() {
        HttpRestTestTemplate.assertGetOk("/times");
    }

    @DisplayName("예약 시간 추가")
    @Test
    void addReservationTime() {
        HttpRestTestTemplate.assertPostCreated(reservationTimeRequest, "/times", "id", notNullValue());
    }

    @DisplayName("예약 시간 삭제")
    @Test
    void deleteReservationTime() {
        HttpRestTestTemplate.assertDeleteNoContent("/times/1");
    }

    @DisplayName("예약 시간 삭제 불가능 - 해당 시간에 예약 존재")
    @Test
    void deleteReservationTimeFailed() {
        HttpRestTestTemplate.assertDeleteBadRequest("/times/2");
    }

    @DisplayName("날짜와 테마에 해당하는 시간 예약 여부 조회")
    @Test
    void getAvailableTimes() {
        HttpRestTestTemplate.assertGetOk("/times/available?date=2024-05-01&themeId=2");
    }
}
