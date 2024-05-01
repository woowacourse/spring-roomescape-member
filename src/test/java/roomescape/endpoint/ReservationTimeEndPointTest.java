package roomescape.endpoint;

import static org.hamcrest.Matchers.notNullValue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationTimeRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ReservationTimeEndPointTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void cleanUp() {
        jdbcTemplate.update("DELETE FROM reservation");
    }

    @DisplayName("[예약 시간 추가 - 예약 추가 - 예약 시간 삭제 불가능] 시나리오")
    @TestFactory
    Stream<DynamicTest> timeAddAndReservationAddAndCannotRemoveTimeScenario() {
        Long reservationTimeId = 1L;
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(
                LocalTime.now()
        );
        ReservationRequest reservationRequest = new ReservationRequest(
                "산초",
                LocalDate.now().plusDays(1),
                reservationTimeId
        );

        return Stream.of(
                DynamicTest.dynamicTest("예약 시간을 등록한다.", () -> {
                    HttpRestTestTemplate.assertPostCreated(reservationTimeRequest, "/times", "id", notNullValue());
                }),

                DynamicTest.dynamicTest("예약을 등록한다.", () -> {
                    HttpRestTestTemplate.assertPostCreated(reservationRequest, "/reservations", "id", notNullValue());
                }),

                DynamicTest.dynamicTest("시간에 해당하는 예약이 있을 경우, 예약 시간을 삭제할 수 없다.", () -> {
                    HttpRestTestTemplate.assertDeleteBadRequest("/times/" + reservationTimeId);
                })
        );
    }
}
