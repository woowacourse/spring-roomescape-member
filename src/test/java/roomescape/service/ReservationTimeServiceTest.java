package roomescape.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static roomescape.TestFixture.MIA_RESERVATION_DATE;
import static roomescape.TestFixture.MIA_RESERVATION_TIME;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.dao.ReservationDao;
import roomescape.domain.reservation.ReservationTime;
import roomescape.dto.reservation.AvailableReservationTimeResponse;
import roomescape.dto.reservation.ReservationTimeResponse;
import roomescape.dao.ReservationTimeDao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {
    @Mock
    private ReservationTimeDao reservationTimeDao;

    @Mock
    ReservationDao reservationDao;

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("예약 시간을 생성한다.")
    void create() {
        // given
        final ReservationTime reservationTime = new ReservationTime(MIA_RESERVATION_TIME);
        final ReservationTime expectedReservationTime = new ReservationTime(1L, reservationTime.getStartAt());

        given(reservationTimeDao.save(any()))
                .willReturn(expectedReservationTime);

        // when
        final ReservationTimeResponse response = reservationTimeService.create(reservationTime);

        // then
        assertAll(
                () -> assertThat(response.id()).isEqualTo(expectedReservationTime.getId()),
                () -> assertThat(response.startAt()).isEqualTo(expectedReservationTime.getStartAt().toString())
        );
    }

    @Test
    @DisplayName("예약 시간 목록을 조회한다.")
    void getAll() {
        // given
        final ReservationTime reservationTime = new ReservationTime(MIA_RESERVATION_TIME);

        given(reservationTimeDao.findAll())
                .willReturn(List.of(reservationTime));

        // when
        final List<ReservationTimeResponse> responses = reservationTimeService.findAll();

        // then
        assertThat(responses).hasSize(1)
                .extracting(ReservationTimeResponse::startAt)
                .contains(MIA_RESERVATION_TIME);
    }

    @Test
    @DisplayName("예약 시간을 삭제한다.")
    void delete() {
        // given
        final ReservationTime reservationTime = new ReservationTime(MIA_RESERVATION_TIME);

        given(reservationTimeDao.findById(anyLong()))
                .willReturn(Optional.of(reservationTime));

        // when & then
        assertThatCode(() -> reservationTimeService.delete(1L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("삭제하려는 예약 시간에 이미 예약이 존재할 경우 예외가 발생한다.")
    void throwExceptionWhenHasReservation() {
        // given
        final ReservationTime reservationTime = new ReservationTime(1L, MIA_RESERVATION_TIME);

        given(reservationTimeDao.findById(anyLong()))
                .willReturn(Optional.of(reservationTime));
        given(reservationDao.countByTimeId(anyLong()))
                .willReturn(1);

        // when & then
        assertThatThrownBy(() -> reservationTimeService.delete(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("선택한 날짜와 테마로 예약 가능한 시간 목록을 조회한다.")
    void findAvailableReservationTimes() {
        // given
        final LocalDate date = LocalDate.parse(MIA_RESERVATION_DATE);
        final Long themeId = 1L;
        final ReservationTime reservedTime = new ReservationTime(1L, MIA_RESERVATION_TIME);

        given(reservationDao.findAllTimeIdsByDateAndThemeId(date, themeId))
                .willReturn(List.of(1L));
        given(reservationTimeDao.findAll())
                .willReturn(List.of(reservedTime, new ReservationTime(2L, "16:00")));

        // when
        final List<AvailableReservationTimeResponse> availableReservationTimes
                = reservationTimeService.findAvailableReservationTimes(date, themeId);

        // then
        assertAll(() -> {
            assertThat(isReserved(availableReservationTimes, MIA_RESERVATION_TIME)).isTrue();
            assertThat(isReserved(availableReservationTimes, "16:00")).isFalse();
        });
    }

    private boolean isReserved(List<AvailableReservationTimeResponse> availableReservationTimes, String time) {
        return availableReservationTimes.stream()
                .filter(response -> response.startAt().equals(time))
                .findFirst()
                .get()
                .isReserved();
    }
}
