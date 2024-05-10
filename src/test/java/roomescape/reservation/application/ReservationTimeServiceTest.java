package roomescape.reservation.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.common.ServiceTest;
import roomescape.global.exception.ViolationException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.dto.response.AvailableReservationTimeResponse;
import roomescape.reservation.dto.response.ReservationTimeResponse;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.TestFixture.*;

class ReservationTimeServiceTest extends ServiceTest {
    @Autowired
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("예약 시간을 생성한다.")
    void create() {
        // given
        ReservationTime reservationTime = new ReservationTime(MIA_RESERVATION_TIME);

        // when
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.create(reservationTime);

        // then
        assertThat(reservationTimeResponse.id()).isNotNull();
    }

    @Test
    @DisplayName("예약 시간 목록을 조회한다.")
    void findAll() {
        // given
        ReservationTime reservationTime = new ReservationTime(MIA_RESERVATION_TIME);
        createTestReservationTime(reservationTime);

        // when
        List<ReservationTimeResponse> responses = reservationTimeService.findAll();

        // then
        assertThat(responses).hasSize(1)
                .extracting(ReservationTimeResponse::startAt)
                .contains(MIA_RESERVATION_TIME);
    }

    @Test
    @DisplayName("예약 시간을 삭제한다.")
    void delete() {
        // given
        ReservationTime reservationTime = new ReservationTime(MIA_RESERVATION_TIME);
        ReservationTime createdReservationTime = createTestReservationTime(reservationTime);

        // when & then
        assertThatCode(() -> reservationTimeService.delete(createdReservationTime.getId()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("삭제하려는 예약 시간에 예약이 존재할 경우 예외가 발생한다.")
    void validateHasReservation() {
        // given
        Reservation reservation = createTestReservation(MIA_RESERVATION());
        Long timeId = reservation.getReservationTimeId();

        // when & then
        assertThatThrownBy(() -> reservationTimeService.delete(timeId))
                .isInstanceOf(ViolationException.class);
    }

    @Test
    @DisplayName("선택한 날짜와 테마로 예약 가능한 시간 목록을 조회한다.")
    void findAvailableReservationTimes() {
        // given
        createTestReservationTime(new ReservationTime(LocalTime.of(16, 0)));
        Reservation reservation = createTestReservation(MIA_RESERVATION());

        // when
        List<AvailableReservationTimeResponse> availableReservationTimes
                = reservationTimeService.findAvailableReservationTimes(MIA_RESERVATION_DATE, reservation.getThemeId());

        // then
        assertAll(() -> {
            assertThat(isReserved(availableReservationTimes, MIA_RESERVATION_TIME)).isTrue();
            assertThat(isReserved(availableReservationTimes, LocalTime.of(16, 0))).isFalse();
        });
    }

    private boolean isReserved(List<AvailableReservationTimeResponse> availableReservationTimes, LocalTime time) {
        return availableReservationTimes.stream()
                .filter(response -> response.startAt().equals(time))
                .findFirst()
                .get()
                .isReserved();
    }
}
