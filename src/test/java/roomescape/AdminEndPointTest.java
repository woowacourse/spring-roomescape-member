package roomescape;

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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

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
        HttpRestTestTemplate.assertGetOk("/reservations", "size()", reservationSize);
    }

    @DisplayName("시간 페이지 응답")
    @Test
    void timePageLoad() {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        int reservationTimeSize = reservationTimes.size();

        HttpRestTestTemplate.assertGetOk("/admin/time");
        HttpRestTestTemplate.assertGetOk("/times", "size()", reservationTimeSize);
    }

    @DisplayName("[예약 시간 추가 - 예약 추가 - 예약 삭제] 시나리오")
    @TestFactory
    Stream<DynamicTest> timeAddAndReservationAddAndRemoveScenario() {
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(
                LocalTime.now()
        );
        ReservationRequest reservationRequest = new ReservationRequest(
                "브라운",
                LocalDate.parse("2023-08-05"),
                1L
        );
        int initialCount = 0;

        return Stream.of(
                DynamicTest.dynamicTest("예약 시간을 등록한다.", () -> {
                    HttpRestTestTemplate.assertPostCreated(reservationTimeRequest, "/times", "id", 1);
                }),

                DynamicTest.dynamicTest("예약을 등록한다.", () -> {
                    HttpRestTestTemplate.assertPostCreated(reservationRequest, "/reservations", "id", 1);
                    Integer countAfterInsert = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
                    assertThat(countAfterInsert).isEqualTo(initialCount + 1);
                }),

                DynamicTest.dynamicTest("예약을 삭제한다.", () -> {
                    HttpRestTestTemplate.assertDeleteNoContent("/reservations/1");
                    Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
                    assertThat(countAfterDelete).isEqualTo(initialCount);
                })
        );
    }

    @DisplayName("[예약 시간 추가 - 예약 추가 - 예약 시간 삭제 불가능] 시나리오")
    @TestFactory
    Stream<DynamicTest> timeAddAndReservationAddAndCannotRemoveTimeScenario() {
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(
                LocalTime.now()
        );
        ReservationRequest reservationRequest = new ReservationRequest(
                "브라운",
                LocalDate.parse("2023-08-05"),
                1L
        );
        int initialCount = 0;

        return Stream.of(
                DynamicTest.dynamicTest("예약 시간을 등록한다.", () -> {
                    HttpRestTestTemplate.assertPostCreated(reservationTimeRequest, "/times", "id", 1);
                }),

                DynamicTest.dynamicTest("예약을 등록한다.", () -> {
                    HttpRestTestTemplate.assertPostCreated(reservationRequest, "/reservations", "id", 1);
                    Integer countAfterInsert = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
                    assertThat(countAfterInsert).isEqualTo(initialCount + 1);
                }),

                DynamicTest.dynamicTest("시간에 해당하는 예약이 있을 경우, 예약 시간을 삭제할 수 없다.", () -> {
                    HttpRestTestTemplate.assertDeleteInitialServerError("/times/1");
                })
        );
    }
}
