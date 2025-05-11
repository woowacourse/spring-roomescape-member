package roomescape.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import roomescape.common.Role;
import roomescape.common.exception.DuplicatedException;
import roomescape.common.exception.NotFoundException;
import roomescape.dao.MemberDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.controller.member.dto.MemberInfoDto;
import roomescape.controller.reservation.dto.MemberReservationRequestDto;
import roomescape.controller.reservation.dto.ReservationResponseDto;
import roomescape.model.Member;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

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

    private ReservationTime time;
    private Theme theme;
    private Member member;

    @BeforeEach
    void setUp() {
        time = new ReservationTime(1L, LocalTime.of(14, 0));
        theme = new Theme(1L, "스릴러", "무서운 테마", "thumbnail.jpg");
        member = new Member(1L, "다로", "email", "password", Role.ADMIN);
    }

    @DisplayName("예약 저장에 성공한다")
    @Test
    void test() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        MemberReservationRequestDto requestDto = new MemberReservationRequestDto(date, 1L, 1L);
        MemberInfoDto memberInfoDto = new MemberInfoDto(1L, Role.ADMIN);

        when(reservationTimeDao.findById(1L)).thenReturn(Optional.of(time));
        when(themeDao.findById(1L)).thenReturn(Optional.of(theme));
        when(reservationDao.findByDateAndTime(any())).thenReturn(Optional.empty());
        when(reservationDao.saveReservation(any())).thenReturn(1L);
        when(memberDao.findById(1L)).thenReturn(Optional.of(member));

        // when
        ReservationResponseDto response = reservationService.saveReservation(requestDto, memberInfoDto);

        // then
        assertThat(response.name()).isEqualTo("다로");
        assertThat(response.date()).isEqualTo(date);
        assertThat(response.time().id()).isEqualTo(1L);
        assertThat(response.theme().name()).isEqualTo("스릴러");
    }

    @DisplayName("존재하지 않는 timeId로 예약 시 예외 발생")
    @Test
    void test1() {
        // given
        MemberReservationRequestDto requestDto = new MemberReservationRequestDto(LocalDate.now().plusDays(1), 999L, 1L);

        when(reservationTimeDao.findById(999L)).thenReturn(Optional.empty());
        MemberInfoDto memberInfoDto = new MemberInfoDto(1L, Role.ADMIN);

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(requestDto, memberInfoDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("예약 시각이 존재하지 않습니다");
    }

    @DisplayName("존재하지 않는 themeId로 예약 시 예외 발생")
    @Test
    void test2() {
        // given
        MemberReservationRequestDto requestDto = new MemberReservationRequestDto(LocalDate.now().plusDays(1), 1L, 999L);
        MemberInfoDto memberInfoDto = new MemberInfoDto(1L, Role.ADMIN);

        when(reservationTimeDao.findById(1L)).thenReturn(Optional.of(time));
        when(themeDao.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(requestDto, memberInfoDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("테마가 존재하지 않습니다");
    }

    @DisplayName("중복된 예약일 경우 예외 발생")
    @Test
    void test3() {
        // given
        MemberReservationRequestDto requestDto = new MemberReservationRequestDto(LocalDate.now().plusDays(1), 1L, 1L);
        MemberInfoDto memberInfoDto = new MemberInfoDto(1L, Role.ADMIN);
        Reservation reservation = requestDto.convertToReservation(
                new Member(1L, "다로", "qwe", "1234", Role.ADMIN),
                time,
                theme
        );

        when(reservationTimeDao.findById(1L)).thenReturn(Optional.of(time));
        when(themeDao.findById(1L)).thenReturn(Optional.of(theme));
        when(reservationDao.findByDateAndTime(any())).thenReturn(Optional.of(reservation));
        when(memberDao.findById(1L)).thenReturn(Optional.of(member));

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(requestDto, memberInfoDto))
                .isInstanceOf(DuplicatedException.class)
                .hasMessageContaining("이미 예약이 존재합니다");
    }

    @DisplayName("전체 예약 목록을 조회한다")
    @Test
    void test4() {
        // given
        Reservation reservation1 = new Reservation(
                1L,
                new Member(1L, "다로", "qwer","1234", Role.ADMIN),
                LocalDate.now().plusDays(2),
                time,
                theme
        );
        Reservation reservation2 = new Reservation(2L,
                new Member(2L, "히로", "qwer", "1234", Role.ADMIN),
                LocalDate.now().plusDays(3),
                time,
                theme
        );

        when(reservationDao.findAll()).thenReturn(List.of(reservation1, reservation2));

        // when
        List<ReservationResponseDto> responses = reservationService.getAllReservations();

        // then
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).name()).isEqualTo("다로");
        assertThat(responses.get(1).name()).isEqualTo("히로");
    }

    @DisplayName("예약을 취소한다")
    @Test
    void test5() {
        // when
        reservationService.cancelReservation(1L);

        // then
        verify(reservationDao, times(1)).deleteById(1L);
    }
}