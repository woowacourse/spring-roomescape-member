package roomescape.endpoint;

import static org.hamcrest.Matchers.notNullValue;
import static roomescape.endpoint.RequestFixture.reservationRequest;
import static roomescape.endpoint.RequestFixture.reservationTimeId;
import static roomescape.endpoint.RequestFixture.reservationTimeRequest;
import static roomescape.endpoint.RequestFixture.themeId;

import java.time.LocalDate;
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
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ReservationEndPointTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ReservationRepository reservationRepository;

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
        Reservation reservation = reservationRepository.save(new Reservation(
                "알파카",
                LocalDate.now(),
                new ReservationTime(
                        1L,
                        LocalTime.parse("10:00:00")
                ),
                new Theme(
                        1L,
                        "이름",
                        "요약",
                        "썸네일"
                )
        ));

        HttpRestTestTemplate.assertDeleteNoContent("/reservations/" + reservation.getId());
    }

    @DisplayName("[예약 시간 추가 - 예약 시간 또는 날짜가 과거라 예약 추가 불가능] 시나리오")
    @TestFactory
    Stream<DynamicTest> validateReservationTimeIsFutureFail() {
        ReservationRequest reservationRequest = new ReservationRequest(
                "알파카",
                LocalDate.now().minusDays(1),
                reservationTimeId,
                themeId
        );

        return Stream.of(
                DynamicTest.dynamicTest("예약 시간을 등록한다.", () -> {
                    HttpRestTestTemplate.assertPostCreated(reservationTimeRequest, "/times", "id",
                            notNullValue());
                }),

                DynamicTest.dynamicTest("지금보다 과거의 시간으로 예약을 등록할 수 없다.", () -> {
                    HttpRestTestTemplate.assertPostBadRequest(reservationRequest, "/reservations");
                })
        );
    }

    @DisplayName("[예약 시간 추가 - 중복 예약 추가 불가능] 시나리오")
    @TestFactory
    Stream<DynamicTest> validateReservationIsDuplicatedFail() {
        return Stream.of(
                DynamicTest.dynamicTest("예약 시간을 등록한다.", () -> {
                    HttpRestTestTemplate.assertPostCreated(reservationTimeRequest, "/times", "id",
                            notNullValue());
                }),

                DynamicTest.dynamicTest("중복되는 예약을 등록한다.", () -> {
                    HttpRestTestTemplate.assertPostCreated(reservationRequest, "/reservations", "id",
                            notNullValue());
                    HttpRestTestTemplate.assertPostBadRequest(reservationRequest, "/reservations");
                })
        );
    }
}
