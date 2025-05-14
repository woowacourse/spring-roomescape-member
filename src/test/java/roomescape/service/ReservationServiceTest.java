package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static roomescape.test.fixture.DateFixture.NEXT_DAY;
import static roomescape.test.fixture.DateFixture.YESTERDAY;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.other.ReservationCreationContent;
import roomescape.exception.BadRequestException;
import roomescape.exception.NotFoundException;
import roomescape.repository.member.MemberRepository;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationtime.ReservationTimeRepository;
import roomescape.repository.theme.ThemeRepository;
import roomescape.test.fixture.ReservationFixture;

class ReservationServiceTest {

    private final ReservationRepository reservationRepository = mock(ReservationRepository.class);
    private final ReservationTimeRepository timeRepository = mock(ReservationTimeRepository.class);
    private final ThemeRepository themeRepository = mock(ThemeRepository.class);
    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final ReservationService reservationService =
            new ReservationService(reservationRepository, timeRepository, themeRepository, memberRepository);

    @DisplayName("저장된 예약들을 조회할 수 있다")
    @Test
    void canGetReservations() {
        // given
        List<Reservation> reservations = List.of(
                ReservationFixture.create(1L, "회원1"),
                ReservationFixture.create(2L, "회원2"),
                ReservationFixture.create(3L, "회원3"));
        when(reservationRepository.findAll()).thenReturn(reservations);

        // when
        List<Reservation> allReservations = reservationService.getAllReservations();

        // then
        assertThat(allReservations).hasSize(3);
    }

    @DisplayName("ID로 예약을 조회할 수 있다")
    @Test
    void canGetReservationById() {
        // given
        Reservation reservation = ReservationFixture.create(1L, "회원1");
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        // when
        Reservation actualReservation = reservationService.getReservationById(1L);

        // then
        assertThat(actualReservation).isEqualTo(reservation);
    }

    @DisplayName("ID로 예약을 조회할 때, 데이터가 없는 경우 예외를 발생시킨다")
    @Test
    void cannotGetReservationByIdBecauseOfEmptyData() {
        // given
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.getReservationById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("[ERROR] ID에 해당하는 예약이 존재하지 않습니다.");
    }

    @DisplayName("예약을 추가할 수 있다")
    @Test
    void canSaveReservation() {
        // given
        long memberId = 1L;
        ReservationCreationContent content = new ReservationCreationContent(memberId, NEXT_DAY, 1L, 1L);
        Member member = new Member(memberId, "회원", "test@test.com", "qdas123!", MemberRole.GENERAL);
        Theme theme = new Theme(content.themeId(), "테마", "설명", "섬네일");
        ReservationTime time = new ReservationTime(content.timeId(), LocalTime.now());
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(themeRepository.findById(content.themeId())).thenReturn(Optional.of(theme));
        when(timeRepository.findById(content.timeId())).thenReturn(Optional.of(time));
        when(reservationRepository.checkAlreadyReserved(content.date(), content.timeId(), content.themeId()))
                .thenReturn(false);
        when(reservationRepository.add(any())).thenReturn(1L);

        // when
        long savedId = reservationService.saveReservation(content);

        // then
        assertThat(savedId).isEqualTo(1L);
    }

    @DisplayName("예약을 추가할 때, 예약 회원이 유효하지 않은 경우 예외를 발생시킨다")
    @Test
    void cannotSaveReservationBecauseOfInvalidMember() {
        // given
        long memberId = 1L;
        ReservationCreationContent content = new ReservationCreationContent(memberId, NEXT_DAY, 1L, 1L);
        Theme theme = new Theme(content.themeId(), "테마", "설명", "섬네일");
        ReservationTime time = new ReservationTime(content.timeId(), LocalTime.now());
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());
        when(themeRepository.findById(content.themeId())).thenReturn(Optional.of(theme));
        when(timeRepository.findById(content.timeId())).thenReturn(Optional.of(time));
        when(reservationRepository.checkAlreadyReserved(content.date(), content.timeId(), content.themeId()))
                .thenReturn(false);
        when(reservationRepository.add(any())).thenReturn(1L);

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(content))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("[ERROR] ID에 해당하는 회원이 존재하지 않습니다.");
    }

    @DisplayName("예약을 추가할 때, 예약 테마가 유효하지 않은 경우 예외를 발생시킨다")
    @Test
    void cannotSaveReservationBecauseOfInvalidTheme() {
        // given
        long memberId = 1L;
        ReservationCreationContent content = new ReservationCreationContent(memberId, NEXT_DAY, 1L, 1L);
        Member member = new Member(memberId, "회원", "test@test.com", "qdas123!", MemberRole.GENERAL);
        ReservationTime time = new ReservationTime(content.timeId(), LocalTime.now());
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(themeRepository.findById(content.themeId())).thenReturn(Optional.empty());
        when(timeRepository.findById(content.timeId())).thenReturn(Optional.of(time));
        when(reservationRepository.checkAlreadyReserved(content.date(), content.timeId(), content.themeId()))
                .thenReturn(false);
        when(reservationRepository.add(any())).thenReturn(1L);

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(content))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("[ERROR] ID에 해당하는 테마가 존재하지 않습니다.");
    }

    @DisplayName("예약을 추가할 때, 예약 시간이 유효하지 않은 경우 예외를 발생시킨다")
    @Test
    void cannotSaveReservationBecauseOfInvalidTime() {
        // given
        long memberId = 1L;
        ReservationCreationContent content = new ReservationCreationContent(memberId, NEXT_DAY, 1L, 1L);
        Member member = new Member(memberId, "회원", "test@test.com", "qdas123!", MemberRole.GENERAL);
        Theme theme = new Theme(content.themeId(), "테마", "설명", "섬네일");
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(themeRepository.findById(content.themeId())).thenReturn(Optional.of(theme));
        when(timeRepository.findById(content.timeId())).thenReturn(Optional.empty());
        when(reservationRepository.checkAlreadyReserved(content.date(), content.timeId(), content.themeId()))
                .thenReturn(false);
        when(reservationRepository.add(any())).thenReturn(1L);

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(content))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("[ERROR] ID에 해당하는 예약시간이 존재하지 않습니다.");
    }

    @DisplayName("예약을 추가할 때, 과거 날짜와 시간으로는 예약을 추가할 수 없다")
    @Test
    void cannotCreateReservationWithPastDateTime() {
        // given
        long memberId = 1L;
        ReservationCreationContent content = new ReservationCreationContent(memberId, YESTERDAY, 1L, 1L);
        Member member = new Member(memberId, "회원", "test@test.com", "qdas123!", MemberRole.GENERAL);
        Theme theme = new Theme(content.themeId(), "테마", "설명", "섬네일");
        ReservationTime time = new ReservationTime(content.timeId(), LocalTime.now().minusSeconds(1));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(themeRepository.findById(content.themeId())).thenReturn(Optional.of(theme));
        when(timeRepository.findById(content.timeId())).thenReturn(Optional.of(time));
        when(reservationRepository.checkAlreadyReserved(content.date(), content.timeId(), content.themeId()))
                .thenReturn(false);
        when(reservationRepository.add(any())).thenReturn(1L);

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(content))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 이미 과거의 날짜와 시간입니다.");
    }

    @DisplayName("예약을 추가할 때, 중복된 예약은 허용하지 않는다")
    @Test
    void cannotCreateReservationWithSameDateTime() {
        // given
        long memberId = 1L;
        ReservationCreationContent content = new ReservationCreationContent(memberId, NEXT_DAY, 1L, 1L);
        Member member = new Member(memberId, "회원", "test@test.com", "qdas123!", MemberRole.GENERAL);
        Theme theme = new Theme(content.themeId(), "테마", "설명", "섬네일");
        ReservationTime time = new ReservationTime(content.timeId(), LocalTime.now());
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(themeRepository.findById(content.themeId())).thenReturn(Optional.of(theme));
        when(timeRepository.findById(content.timeId())).thenReturn(Optional.of(time));
        when(reservationRepository.checkAlreadyReserved(content.date(), content.timeId(), content.themeId()))
                .thenReturn(true);
        when(reservationRepository.add(any())).thenReturn(1L);

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(content))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 이미 존재하는 예약입니다.");
    }

    @DisplayName("ID를 통해 예약을 삭제할 수 있다.")
    @Test
    void canDeleteReservation() {
        // given
        Reservation reservation = ReservationFixture.create(1L, "회원");
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));

        // when & then
        assertAll(
                () -> assertThatCode(() -> reservationService.deleteReservation(reservation.getId()))
                        .doesNotThrowAnyException(),
                () -> verify(reservationRepository, times(1)).deleteById(reservation.getId())
        );
    }

    @DisplayName("존재하지 않는 예약을 삭제하려고 할 경우 예외를 발생시킨다")
    @Test
    void cannotDeleteNoneExistentReservation() {
        // given
        long noneExistentId = 1L;
        when(reservationRepository.findById(noneExistentId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.deleteReservation(noneExistentId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("[ERROR] ID에 해당하는 예약이 존재하지 않습니다.");
    }
}
