package roomescape.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.domain.reservation.dto.ReservationTimeRequest;
import roomescape.domain.reservation.dto.ReservationTimeResponse;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.repository.fake.FakeReservationRepository;
import roomescape.domain.reservation.repository.fake.FakeReservationTimeRepository;

public class ReservationTimeServiceTest {

    private final FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
    private final FakeReservationRepository reservationRepository = new FakeReservationRepository();
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        reservationTimeRepository.deleteAll();
        reservationTimeService = new ReservationTimeService(reservationTimeRepository, reservationRepository);
    }

    @DisplayName("모든 시간 정보를 가져온다.")
    @Test
    void test1() {
        // given
        final LocalTime localTime1 = LocalTime.of(8, 0);
        final LocalTime localTime2 = LocalTime.of(9, 0);
        final List<LocalTime> localTimes = List.of(localTime1, localTime2);

        for (final LocalTime localTime : localTimes) {
            reservationTimeRepository.save(ReservationTime.withoutId(localTime));
        }

        // when
        final List<ReservationTimeResponse> result = reservationTimeService.getAll();

        // then
        final List<LocalTime> resultTimes = result.stream()
                .map(ReservationTimeResponse::startAt)
                .toList();

        assertThat(resultTimes).containsExactlyInAnyOrderElementsOf(localTimes);
    }

    @DisplayName("정보가 없다면 빈 리스트를 반환한다.")
    @Test
    void test2() {
        // given & when
        final List<ReservationTimeResponse> result = reservationTimeService.getAll();

        // then
        assertThat(result).isEmpty();
    }

    @DisplayName("예약 시간을 저장한다.")
    @Test
    void test3() {
        // given
        final LocalTime localTime1 = LocalTime.of(8, 0);
        final ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(localTime1);

        // when
        final ReservationTimeResponse result = reservationTimeService.create(reservationTimeRequest);

        // then
        final SoftAssertions softly = new SoftAssertions();

        softly.assertThat(result.id())
                .isNotNull();
        softly.assertThat(result.startAt())
                .isEqualTo(localTime1);

        softly.assertAll();
    }

    @DisplayName("예약 시간을 삭제한다")
    @Test
    void test4() {
        // given
        final ReservationTime saved = reservationTimeRepository.save(ReservationTime.withoutId(LocalTime.of(8, 0)));
        final Long id = saved.getId();

        // when & then
        assertThatCode(() -> reservationTimeService.delete(id)).doesNotThrowAnyException();

        assertThat(reservationRepository.findById(id)).isEmpty();
    }

    @DisplayName("예약 시간이 존재하지 않으면 예외를 반환한다.")
    @Test
    void test5() {
        final Long id = 1L;

        assertThatThrownBy(() -> reservationTimeService.delete(id)).isInstanceOf(EntityNotFoundException.class);
    }
}
