package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.Theme;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.SelectableTimeResponse;
import roomescape.repository.MemberDao;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;
import roomescape.repository.ThemeDao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    @Mock
    private ReservationDao reservationDao;

    @Mock
    private ReservationTimeDao reservationTimeDao;

    @Mock
    private ThemeDao themeDao;

    @Mock
    private MemberDao memberDao;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation dummyReservation;

    @BeforeEach
    void setUp() {
        dummyReservation = new Reservation(
                1L,
                LocalDate.now(),
                new ReservationTime(1L, LocalTime.now()),
                new Theme(1L, "테마", "테마입니다.", "123"),
                new Member(1L, "콜리", "kkwoo001021@naver.com", "ralrjsdn1021!", "USER")
        );
    }

    //TODO save는 실제 테스트 고려하기 -> Mock stubbing이 너무 많이 소요됨

//    @Test
//    @DisplayName("실패 : 이전 날짜 예약을 저장할 수 없다")
//    void should_ThrowIllegalArgumentException_When_GivePastDateReservation() {
//        //given
//        LocalDate past = LocalDate.now().minusDays(1);
//
//        //when-then
//        assertThatThrownBy(() -> reservationService.save(past, 1L, 1L, 1L))
//                .isInstanceOf(IllegalArgumentException.class);
//    }

    @Test
    @DisplayName("예약 내역을 모두 조회할 수 있다.")
    void should_FindAllReservations() {
        //given
        int expectedSize = 1;

        Mockito.when(reservationDao.getAll())
                .thenReturn(List.of(dummyReservation));

        //when
        List<ReservationResponse> reservations = reservationService.findAll();

        //then
        assertAll(
                () -> verify(reservationDao, times(1)).getAll(),
                () -> assertThat(reservations).hasSize(expectedSize)
        );
    }

    @Test
    @DisplayName("성공 : id에 해당하는 예약을 삭제할 수 있다.")
    void should_SuccessDeleteReservation_WhenGiveId() {
        //given
        ArgumentCaptor<Long> valueCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.when(reservationDao.findById(anyLong()))
                .thenReturn(Optional.of(dummyReservation));
        Mockito.doNothing()
                .when(reservationDao).delete(valueCaptor.capture());

        //when
        reservationService.delete(1L);

        //then
        Mockito.verify(reservationDao, times(1)).delete(anyLong());
        assertThat(valueCaptor.getValue()).isEqualTo(1L);
    }

    @Test
    @DisplayName("성공 : id에 해당하는 예약이 없다면 삭제에 실패한다.")
    void delete() {
        //given
        Mockito.when(reservationDao.findById(anyLong()))
                .thenReturn(Optional.ofNullable(null));

        //when-then
        assertThatThrownBy(() -> reservationService.delete(1L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("특정 날짜와 테마에서 예약된 시간이라면 예약이 되었음을 조회할 수 있다")
    void should_JudgeAlreadyBookedTime_When_Give_Date_And_ThemeI() {
        //given
        ReservationTime timeDummy = new ReservationTime(1L, LocalTime.of(9, 0, 0));

        Mockito.when(reservationDao.findByDateAndThemeId(eq(LocalDate.now()), eq(1L)))
                .thenReturn(List.of(dummyReservation));
        Mockito.when(reservationTimeDao.getAll())
                .thenReturn(List.of(timeDummy));

        //when
        List<SelectableTimeResponse> selectableTimes = reservationService.findSelectableTime(LocalDate.now(), 1L);

        //then
        assertThat(selectableTimes.get(0).alreadyBooked()).isTrue();
    }

    @Test
    @DisplayName("특정 날짜와 테마에서 예약되지 않은 시간이라면 예약되지 않았음을 초기화한다")
    void should_JudgeNotBookedTime_When_Give_Date_And_ThemeId() {
        //given
        ReservationTime timeDummy = new ReservationTime(2L, LocalTime.of(10, 0, 0));

        Mockito.when(reservationDao.findByDateAndThemeId(any(LocalDate.class), anyLong()))
                .thenReturn(List.of());
        Mockito.when(reservationTimeDao.getAll())
                .thenReturn(List.of(timeDummy));

        //when
        List<SelectableTimeResponse> selectableTimes = reservationService.findSelectableTime(LocalDate.now(), 1L);

        //then
        assertThat(selectableTimes.get(0).alreadyBooked()).isFalse();

    }

    @Test
    @DisplayName("예약 시간 id + 날짜 + 기간에 해당하는 예약을 조회할 수 있다")
    void should_FindReservationByTimeAndDateInDuration() {
        //when
        reservationService.findReservationByTimeAndDateInDuration(
                1L,
                1L,
                LocalDate.now().minusWeeks(1),
                LocalDate.now()
        );

        //then
        verify(reservationDao, times(1))
                .findByThemeIdAndMemberIdInDuration(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class));
    }
}