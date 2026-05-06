package roomescape.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationRepository;
import roomescape.entity.ReservationTime;
import roomescape.entity.ReservationTimeRepository;
import roomescape.fake.FakeReservationRepository;
import roomescape.fake.FakeReservationTimeRepository;
import roomescape.global.exception.customException.ReservationTimeException;

class ReservationTimeServiceTest {

    private final ReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
    private final ReservationRepository reservationRepository = new FakeReservationRepository();

    private ReservationTimeService reservationTimeService;


    @BeforeEach
    void setUp() {
        reservationTimeService = new ReservationTimeService(reservationTimeRepository, reservationRepository);
    }

    @Test
    @DisplayName("예약 시간을 저장한다")
    void save_success() {
        // given
        LocalTime testStartAt = LocalTime.now();

        // when & then
        Assertions.assertDoesNotThrow(
                () -> reservationTimeService.save(testStartAt)
        );
    }

    @Test
    @DisplayName("예약 시간 목록을 조회 시 오류가 발생하지 않음")
    void findAll_success() {
        // given
        LocalTime testStartAt = LocalTime.now();
        reservationTimeService.save(testStartAt);

        // when & then
        Assertions.assertDoesNotThrow(
                () -> reservationTimeService.findAll()
        );
    }

    @Test
    @DisplayName("예약 시간이 없으도 오류를 발생시키지 않음")
    void findAllWhenEmpty() {
        // when & then
        Assertions.assertDoesNotThrow(
                () -> reservationTimeService.findAll()
        );
    }

    @Test
    @DisplayName("예약 시간을 삭제 시 값이 있으면 오류를 발생시키지 않음")
    void deleteById() {
        // given
        LocalTime testStartAt = LocalTime.now();
        ReservationTime saved = reservationTimeService.save(testStartAt);

        Assertions.assertDoesNotThrow(
                () -> reservationTimeService.deleteById(saved.id())
        );
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간을 삭제해도 예외가 발생하지 않는다")
    void deleteNotFoundTime() {
        // when & then
        Assertions.assertDoesNotThrow(
                () -> reservationTimeService.deleteById(999L)
        );
    }

    @Test
    @DisplayName("예약 시간 id가 참조되고 있으면 삭제할 때 예외가 발생한다")
    void deleteTimeWithReferencedReservationById() {
        // given
        LocalTime testStartAt = LocalTime.now();
        ReservationTime savedReservationTime = reservationTimeService.save(testStartAt);
        reservationRepository.save(Reservation.createWithNullId("테스터", LocalDate.now(), savedReservationTime, null));

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteById(savedReservationTime.id()))
                .isInstanceOf(ReservationTimeException.class);
    }
}
