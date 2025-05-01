package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.dao.reservationTime.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.response.ReservationTimeUserResponse;
import roomescape.exception.ReservationExistException;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @Mock
    ReservationTimeDao reservationTimeDao;

    @InjectMocks
    ReservationTimeService reservationTimeService;

    @DisplayName("존재하지 않는 id로 예약 시간을 찾을 경우 예외가 발생한다.")
    @Test
    void findByIdThrowExceptionIfIdIsNotExist() {

        // given
        when(reservationTimeDao.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationTimeService.findById(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("예약 시간이 존재하지 않습니다.");
    }

    @DisplayName("특정 날짜와 테마를 사용하는 시간을 조회한다.")
    @Test
    void findAllByDateAndThemeTest() {

        // given
        LocalDate date = LocalDate.now();
        Optional<ReservationTime> reservationTime = Optional.of(new ReservationTime(1L, LocalTime.of(10, 10)));
        when(reservationTimeDao.findByIdAndDateAndTheme(1L, 1L, date)).thenReturn(reservationTime);
        when(reservationTimeDao.findAll()).thenReturn(List.of(reservationTime.get()));

        // when
        List<ReservationTimeUserResponse> reservationTimeUserResponses = reservationTimeService.findAllByDateAndTheme(
                1L, date);

        // then
        assertThat(reservationTimeUserResponses.getFirst()).isEqualTo(
                ReservationTimeUserResponse.from(reservationTime.get(), true));
    }

    @DisplayName("예약 시간이 사용되지 않는 경우 삭제한다.")
    @Test
    void deleteIfNoReservationTest() {

        // given
        when(reservationTimeDao.findById(1L)).thenReturn(Optional.of(new ReservationTime(1L, LocalTime.of(10, 10))));
        when(reservationTimeDao.deleteIfNoReservation(1L)).thenReturn(true);

        // when & then
        assertThatCode(() -> reservationTimeService.deleteIfNoReservation(1L))
                .doesNotThrowAnyException();
    }

    @DisplayName("예약 시간이 사용되는 경우 삭제시 예외가 발생한다.")
    @Test
    void deleteIfNoReservationThrowExceptionTest() {

        // given
        when(reservationTimeDao.findById(1L)).thenReturn(Optional.of(new ReservationTime(1L, LocalTime.of(10, 10))));
        when(reservationTimeDao.deleteIfNoReservation(1L)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteIfNoReservation(1L))
                .isInstanceOf(ReservationExistException.class)
                .hasMessage("이 시간에 대한 예약이 존재합니다.");
    }
}
