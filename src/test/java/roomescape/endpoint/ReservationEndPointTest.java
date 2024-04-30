package roomescape.endpoint;

import static org.hamcrest.Matchers.notNullValue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationTimeRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationEndPointTest {

    @DisplayName("[예약 시간 추가 - 예약 추가 - 예약 삭제] 시나리오")
    @TestFactory
    Stream<DynamicTest> timeAddAndReservationAddAndRemoveScenario() {
        Long reservationTimeId = 1L;
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(
                LocalTime.now()
        );
        ReservationRequest reservationRequest = new ReservationRequest(
                "알파카",
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

                DynamicTest.dynamicTest("예약을 삭제한다.", () -> {
                    HttpRestTestTemplate.assertDeleteNoContent("/reservations/" + reservationTimeId);
                })
        );
    }

    @DisplayName("[예약 시간 추가 - 예약 시간 또는 날짜가 과거라 예약 추가 불가능] 시나리오")
    @TestFactory
    Stream<DynamicTest> validateReservationTimeIsFutureFail() {
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(
                LocalTime.now().minusHours(1)
        );
        ReservationRequest reservationRequest = new ReservationRequest(
                "러너덕",
                LocalDate.now().minusDays(1),
                1L
        );

        return Stream.of(
                DynamicTest.dynamicTest("예약 시간을 등록한다.", () -> {
                    HttpRestTestTemplate.assertPostCreated(reservationTimeRequest, "/times", "id", notNullValue());
                }),

                DynamicTest.dynamicTest("지금보다 과거의 시간으로 예약을 등록할 수 없다.", () -> {
                    HttpRestTestTemplate.assertPostBadRequest(reservationRequest, "/reservations");
                })
        );
    }

    @DisplayName("[예약 시간 추가 - 중복 예약 추가 불가능] 시나리오")
    @TestFactory
    Stream<DynamicTest> validateReservationIsDuplicatedFail() {
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(
                LocalTime.now()
        );
        ReservationRequest reservationRequest = new ReservationRequest(
                "찰리",
                LocalDate.now().plusDays(1),
                1L
        );

        return Stream.of(
                DynamicTest.dynamicTest("예약 시간을 등록한다.", () -> {
                    HttpRestTestTemplate.assertPostCreated(reservationTimeRequest, "/times", "id", notNullValue());
                }),

                DynamicTest.dynamicTest("중복되는 예약을 등록한다.", () -> {
                    HttpRestTestTemplate.assertPostCreated(reservationRequest, "/reservations", "id", notNullValue());
                    HttpRestTestTemplate.assertPostBadRequest(reservationRequest, "/reservations");
                })
        );
    }
}
