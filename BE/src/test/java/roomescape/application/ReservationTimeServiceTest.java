package roomescape.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.global.exception.customException.ReservationTimeException;
import roomescape.fake.FakeReservationRepository;
import roomescape.fake.FakeReservationTimeRepository;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTimeRepository;

class ReservationTimeServiceTest {

    private final  ReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
    private final ReservationRepository reservationRepository = new FakeReservationRepository();

    private ReservationTimeService reservationTimeService;


    @BeforeEach
    void setUp() {
        reservationTimeService = new ReservationTimeService(reservationTimeRepository, reservationRepository);
    }

    @Test
    @DisplayName("예약 시간을 저장한다")
    void saveTime_success() {
        // given
        LocalTime testStartAt = LocalTime.now();

        // when & then
        Assertions.assertDoesNotThrow(
                () -> reservationTimeService.saveTime(testStartAt)
        );
    }

    @Test
    @DisplayName("예약 시간 목록을 조회 시 오류가 발생하지 않음")
    void getTimes_success() {
        // given
        LocalTime testStartAt = LocalTime.now();
        reservationTimeService.saveTime(testStartAt);

        // when & then
        Assertions.assertDoesNotThrow(
                () -> reservationTimeService.getTimes()
        );
    }

    @Test
    @DisplayName("예약 시간이 없으도 오류를 발생시키지 않음")
    void getTimesWhenEmpty() {
        // when & then
        Assertions.assertDoesNotThrow(
                () -> reservationTimeService.getTimes()
        );
    }

    @Test
    @DisplayName("예약 시간을 삭제 시 값이 있으면 오류를 발생시키지 않음")
    void deleteTime() {
        // given
        LocalTime testStartAt = LocalTime.now();
        ReservationTime saved = reservationTimeService.saveTime(testStartAt);

        Assertions.assertDoesNotThrow(
                () -> reservationTimeService.deleteTime(saved.id())
        );
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간을 삭제해도 예외가 발생하지 않는다")
    void deleteNotFoundTime() {
        // when & then
        Assertions.assertDoesNotThrow(
                () -> reservationTimeService.deleteTime(999L)
        );
    }

    @Test
    @DisplayName("예약 시간 id가 참조되고 있으면 삭제할 때 예외가 발생한다")
    void deleteTimeWithReferencedReservationTime() {
        // given
        LocalTime testStartAt = LocalTime.now();
        ReservationTime savedReservationTime = reservationTimeService.saveTime(testStartAt);
        reservationRepository.save(Reservation.createWithNullId("테스터", LocalDate.now(), savedReservationTime, null));

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteTime(savedReservationTime.id()))
                .isInstanceOf(ReservationTimeException.class);
    }
}
