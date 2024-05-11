package roomescape.service;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.Fixtures;
import roomescape.exception.BadRequestException;
import roomescape.exception.IllegalTimeException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.domain.member.MemberRepository;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.reservationtime.ReservationTimeRepository;
import roomescape.domain.theme.ThemeRepository;
import roomescape.service.reservation.ReservationService;
import roomescape.service.reservation.dto.ReservationCreateRequest;
import roomescape.service.reservation.dto.ReservationResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@DisplayName("예약 서비스")
class ReservationServiceTest {
    @InjectMocks
    private ReservationService reservationService;
    @Mock
    private ReservationTimeRepository reservationTimeRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private ThemeRepository themeRepository;
    @Mock
    private MemberRepository memberRepository;


    @DisplayName("예약 서비스는 예약들을 조회한다.")
    @Test
    void readReservations() {
        // given
        Mockito.when(reservationRepository.findAll())
                .thenReturn(List.of(Fixtures.reservationFixture));

        // when
        List<ReservationResponse> reservations = reservationService.readReservations();

        // then
        assertThat(reservations).hasSize(1);
    }

    @DisplayName("예약 서비스는 id에 맞는 예약을 조회한다.")
    @Test
    void readReservation() {
        // given
        Mockito.when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(Fixtures.reservationFixture));

        // when
        ReservationResponse reservation = reservationService.readReservation(1L);

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(reservation.date()).isEqualTo(Fixtures.DATE_AFTER_6_MONTH_LATER);
        softAssertions.assertThat(reservation.member().id()).isEqualTo(1L);
        softAssertions.assertAll();
    }

    @DisplayName("예약 서비스는 예약을 생성한다.")
    @Test
    void createReservation() {
        // given
        Mockito.when(memberRepository.findById(1L))
                .thenReturn(Optional.of(Fixtures.memberFixture));
        Mockito.when(reservationTimeRepository.findById(1L))
                .thenReturn(Optional.of(Fixtures.reservationTimeFixture));
        Mockito.when(themeRepository.findById(1L))
                .thenReturn(Optional.of(Fixtures.themeFixture));
        ReservationCreateRequest request = new ReservationCreateRequest(
                1L, Fixtures.DATE_AFTER_6_MONTH_LATER, 1L, 1L
        );
        Mockito.when(reservationRepository.save(any()))
                .thenReturn(Fixtures.reservationFixture);

        // when
        ReservationResponse reservation = reservationService.createReservation(request);

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(reservation.date()).isEqualTo(Fixtures.DATE_AFTER_6_MONTH_LATER);
        softAssertions.assertThat(reservation.member().id()).isEqualTo(1L);
        softAssertions.assertThat(reservation.time().startAt()).isEqualTo(LocalTime.of(10, 10));
        softAssertions.assertAll();
    }

    @DisplayName("예약 서비스는 지난 시점의 예약이 요청되면 예외가 발생한다.")
    @Test
    void validateRequestedTime() {
        // given
        Mockito.when(memberRepository.findById(2L))
                .thenReturn(Optional.of(Fixtures.memberFixture));
        Mockito.when(reservationTimeRepository.findById(1L))
                .thenReturn(Optional.of(Fixtures.reservationTimeFixture));
        Mockito.when(themeRepository.findById(1L))
                .thenReturn(Optional.of(Fixtures.themeFixture));

        LocalDate date = LocalDate.MIN;
        ReservationCreateRequest request = new ReservationCreateRequest(
                2L, date, 1L, 1L
        );

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(IllegalTimeException.class)
                .hasMessage("이미 지난 날짜는 예약할 수 없습니다.");
    }

    @DisplayName("예약 서비스는 중복된 예약 요청이 들어오면 예외가 발생한다.")
    @Test
    void validateIsDuplicated() {
        // given
        Mockito.when(memberRepository.findById(1L))
                .thenReturn(Optional.of(Fixtures.memberFixture));
        Mockito.when(reservationTimeRepository.findById(1L))
                .thenReturn(Optional.of(Fixtures.reservationTimeFixture));
        Mockito.when(themeRepository.findById(1L))
                .thenReturn(Optional.of(Fixtures.themeFixture));
        Mockito.when(reservationRepository.existsBy(any(), any(), any()))
                .thenReturn(true);
        ReservationCreateRequest request = new ReservationCreateRequest(
                1L,
                Fixtures.reservationFixture.getDate(),
                1L,
                1L
        );

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("해당 시간대에 이미 예약된 테마입니다.");
    }

    @DisplayName("예약 서비스는 예약 요청에 존재하지 않는 시간이 포함된 경우 예외가 발생한다.")
    @Test
    void createWithNonExistentTime() {
        // given
        Mockito.when(memberRepository.findById(2L))
                .thenReturn(Optional.of(Fixtures.memberFixture));
        Mockito.when(reservationTimeRepository.findById(1L))
                .thenReturn(Optional.empty());

        ReservationCreateRequest request = new ReservationCreateRequest(
                2L, Fixtures.DATE_AFTER_6_MONTH_LATER, 1L, 1L
        );

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("존재하지 않는 예약 시간입니다.");
    }

    @DisplayName("예약 서비스는 요청받은 테마가 동시간대에 이미 예약된 경우 예외가 발생한다.")
    @Test
    void createWithReservedTheme() {
        // given
        Mockito.when(memberRepository.findById(1L))
                .thenReturn(Optional.of(Fixtures.memberFixture));
        Mockito.when(reservationTimeRepository.findById(1L))
                .thenReturn(Optional.of(Fixtures.reservationTimeFixture));
        Mockito.when(themeRepository.findById(1L))
                .thenReturn(Optional.of(Fixtures.themeFixture));
        Mockito.when(reservationRepository.existsBy(any(), any(), any()))
                .thenReturn(true);
        ReservationCreateRequest request = new ReservationCreateRequest(
                1L, Fixtures.DATE_AFTER_6_MONTH_LATER, 1L, 1L
        );

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(IllegalTimeException.class)
                .hasMessage("해당 시간대에 이미 예약된 테마입니다.");
    }

    @DisplayName("예약 서비스는 id에 맞는 예약을 삭제한다.")
    @Test
    void deleteReservation() {
        // given
        Mockito.doNothing().when(reservationRepository).deleteById(1L);

        // when & then
        assertThatCode(() -> reservationService.deleteReservation(1L))
                .doesNotThrowAnyException();
    }
}
