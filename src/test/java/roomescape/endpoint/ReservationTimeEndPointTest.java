package roomescape.endpoint;

import static org.hamcrest.Matchers.notNullValue;
import static roomescape.endpoint.RequestFixture.reservationTimeRequest;

import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ReservationTimeEndPointTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

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
        ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(null, LocalTime.now()));

        HttpRestTestTemplate.assertDeleteNoContent("/times/" + reservationTime.getId());
    }

    @DisplayName("[예약이 존재할 때, 예약 시간 삭제 불가능] 시나리오")
    @TestFactory
    Stream<DynamicTest> timeAddAndReservationAddAndCannotRemoveTimeScenario() {
        return Stream.of(
                DynamicTest.dynamicTest("예약 시간을 등록한다.", () -> {
                    HttpRestTestTemplate.assertPostCreated(RequestFixture.reservationTimeRequest, "/times", "id",
                            notNullValue());
                }),

                DynamicTest.dynamicTest("예약을 등록한다.", () -> {
                    HttpRestTestTemplate.assertPostCreated(RequestFixture.reservationRequest, "/reservations", "id",
                            notNullValue());
                }),

                DynamicTest.dynamicTest("시간에 해당하는 예약이 있을 경우, 예약 시간을 삭제할 수 없다.", () -> {
                    HttpRestTestTemplate.assertDeleteBadRequest("/times/" + RequestFixture.reservationTimeId);
                })
        );
    }

    @DisplayName("날짜에 해당하는 테마 시간 예약 여부 조회")
    @Test
    void getAvailableTimes() {
        HttpRestTestTemplate.assertGetOk("/times/available?date=2024-06-03&themeId=1");
    }
}
