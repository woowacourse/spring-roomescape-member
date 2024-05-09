package roomescape.reservation.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.global.exception.ViolationException;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.ReservationTimeRepository;
import roomescape.reservation.dto.response.AvailableReservationTimeResponse;
import roomescape.reservation.dto.response.ReservationTimeResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static roomescape.TestFixture.MIA_RESERVATION_DATE;
import static roomescape.TestFixture.MIA_RESERVATION_TIME;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {
    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("예약 시간을 생성한다.")
    void create() {
        // given
        ReservationTime reservationTime = new ReservationTime(MIA_RESERVATION_TIME);
        ReservationTime expectedReservationTime = new ReservationTime(1L, reservationTime);

        BDDMockito.given(reservationTimeRepository.save(any()))
                .willReturn(expectedReservationTime);

        // when
        ReservationTimeResponse response = reservationTimeService.create(reservationTime);

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
        ReservationTime reservationTime = new ReservationTime(MIA_RESERVATION_TIME);

        BDDMockito.given(reservationTimeRepository.findAll())
                .willReturn(List.of(reservationTime));

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

        BDDMockito.given(reservationTimeRepository.findById(anyLong()))
                .willReturn(Optional.of(reservationTime));

        // when & then
        assertThatCode(() -> reservationTimeService.delete(1L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("삭제하려는 예약 시간에 예약이 존재할 경우 예외가 발생한다.")
    void validateHasReservation() {
        // given
        ReservationTime reservationTime = new ReservationTime(1L, MIA_RESERVATION_TIME);

        BDDMockito.given(reservationTimeRepository.findById(anyLong()))
                .willReturn(Optional.of(reservationTime));
        BDDMockito.given(reservationRepository.countByTimeId(anyLong()))
                .willReturn(1);

        // when & then
        assertThatThrownBy(() -> reservationTimeService.delete(1L))
                .isInstanceOf(ViolationException.class);
    }

    @Test
    @DisplayName("선택한 날짜와 테마로 예약 가능한 시간 목록을 조회한다.")
    void findAvailableReservationTimes() {
        // given
        LocalDate date = MIA_RESERVATION_DATE;
        Long themeId = 1L;
        ReservationTime reservedTime = new ReservationTime(1L, MIA_RESERVATION_TIME);

        BDDMockito.given(reservationRepository.findAllTimeIdsByDateAndThemeId(date, themeId))
                .willReturn(List.of(1L));
        BDDMockito.given(reservationTimeRepository.findAll())
                .willReturn(List.of(reservedTime, new ReservationTime(2L, LocalTime.of(16, 0))));

        // when
        List<AvailableReservationTimeResponse> availableReservationTimes
                = reservationTimeService.findAvailableReservationTimes(date, themeId);

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
