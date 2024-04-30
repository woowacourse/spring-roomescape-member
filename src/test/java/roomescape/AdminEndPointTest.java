package roomescape;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationTimeRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminEndPointTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("관리자 메인 페이지 응답")
    @Test
    void adminPageLoad() {
        HttpRestTestTemplate.assertGetOk("/admin");
    }

    @DisplayName("예약 페이지 응답")
    @Test
    void reservationPageLoad() {
        List<Reservation> reservations = reservationRepository.findAll();
        int reservationSize = reservations.size();

        HttpRestTestTemplate.assertGetOk("/admin/reservation");
        HttpRestTestTemplate.assertGetOk("/reservations", "size()", is(reservationSize));
    }

    @DisplayName("시간 페이지 응답")
    @Test
    void timePageLoad() {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        int reservationTimeSize = reservationTimes.size();

        HttpRestTestTemplate.assertGetOk("/admin/time");
        HttpRestTestTemplate.assertGetOk("/times", "size()", is(reservationTimeSize));
    }

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


    @DisplayName("빈 값 입력 검증")
    @Test
    void validateNotNull() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "");
        params.put("timeId", "1");

        HttpRestTestTemplate.assertPostBadRequest(params, "/reservations");
    }
}
