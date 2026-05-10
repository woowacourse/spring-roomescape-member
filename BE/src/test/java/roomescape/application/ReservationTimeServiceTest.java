package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.fake.FakeReservationRepository;
import roomescape.fake.FakeReservationTimeRepository;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTimeRepository;
import roomescape.global.exception.BusinessException;

class ReservationTimeServiceTest {

    private ReservationTimeRepository reservationTimeRepository;
    private ReservationRepository reservationRepository;
    private ReservationTimeService reservationTimeService;


    @BeforeEach
    void setUp() {
        reservationTimeRepository = new FakeReservationTimeRepository();
        reservationRepository = new FakeReservationRepository();
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
    @DisplayName("날짜와 테마 기준으로 예약된 시간을 조회한다")
    void getBookedTimes() {
        // given
        ReservationTime bookedTime = reservationTimeService.saveTime(LocalTime.of(10, 0));
        reservationTimeService.saveTime(LocalTime.of(11, 0));
        Theme theme = Theme.createRow(1L, "공포", "설명", "https://good.com/thumb-nail/1");
        LocalDate date = LocalDate.now();
        reservationRepository.save(Reservation.create("테스터", date, bookedTime, theme));

        // when
        List<ReservationTime> result = reservationTimeService.getBookedTimes(date, theme.id());

        // then
        assertThat(result)
                .extracting(ReservationTime::id)
                .containsExactlyInAnyOrder(bookedTime.id());
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
        reservationRepository.save(Reservation.create("테스터", LocalDate.now(), savedReservationTime, null));

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteTime(savedReservationTime.id()))
                .isInstanceOf(BusinessException.class);
    }
}
