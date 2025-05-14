package roomescape.reservationTime.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.exception.ConflictException;
import roomescape.exception.NotFoundException;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.dao.ReservationTimeDao;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.ReservationTimeUserResponse;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.theme.domain.Theme;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @Mock
    ReservationTimeDao reservationTimeDao;

    @Mock
    ReservationDao reservationDao;

    @InjectMocks
    ReservationTimeService reservationTimeService;


    @DisplayName("존재하지 않는 id로 예약 시간을 찾을 경우 예외가 발생한다.")
    @Test
    void findByIdThrowExceptionIfIdIsNotExist() {

        // given
        when(reservationTimeDao.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationTimeService.findById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("예약 시간이 존재하지 않습니다.");
    }

    @DisplayName("특정 날짜와 테마를 사용하는 시간을 조회한다.")
    @Test
    void findAllByDateAndThemeTest() {

        // given
        LocalDate date = LocalDate.now();
        Optional<ReservationTime> reservationTime = Optional.of(ReservationTime.load(1L, LocalTime.of(10, 10)));
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
        ReservationTime reservationTime = ReservationTime.load(1L, LocalTime.of(10, 10));
        when(reservationTimeDao.findById(1L)).thenReturn(Optional.of(reservationTime));
        when(reservationDao.findByTimeId(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatCode(() -> reservationTimeService.deleteIfNoReservation(1L))
                .doesNotThrowAnyException();
    }

    @DisplayName("예약 시간이 사용되는 경우 삭제시 예외가 발생한다.")
    @Test
    void deleteIfNoReservationThrowExceptionTest() {

        // given
        ReservationTime reservationTime = ReservationTime.load(1L, LocalTime.of(10, 10));
        when(reservationTimeDao.findById(any(Long.class))).thenReturn(Optional.of(reservationTime));
        when(reservationDao.findByTimeId(any(Long.class))).thenReturn(Optional.of(
                Reservation.load(1L, LocalDate.now(),
                        new Member(1L, "test", "test@test.com", "qwer1234!", Role.USER),
                        reservationTime,
                        Theme.load(1L, "test", "test", "test"))));
        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteIfNoReservation(1L))
                .isInstanceOf(ConflictException.class)
                .hasMessage("이 시간에 대한 예약이 존재합니다.");
    }
}
