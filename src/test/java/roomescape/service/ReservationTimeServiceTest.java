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
import roomescape.domain.Reservation;
import roomescape.exception.IllegalTimeException;
import roomescape.exception.ReferencedReservationExistException;
import roomescape.service.dto.reservationtime.ReservationTimeCreateRequest;
import roomescape.service.dto.reservationtime.ReservationTimeResponse;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationtime.ReservationTimeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@DisplayName("예약 시간 서비스")
class ReservationTimeServiceTest {
    @InjectMocks
    private ReservationTimeService reservationTimeService;
    @Mock
    private ReservationTimeRepository reservationTimeRepository;
    @Mock
    private ReservationRepository reservationRepository;

    @DisplayName("에약 시간 서비스는 시간을 생성한다.")
    @Test
    void createTime() {
        // given
        Mockito.when(reservationTimeRepository.save(any()))
                .thenReturn(Fixtures.reservationTimeFixture);
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(Fixtures.TIME_10_10);

        // when
        ReservationTimeResponse reservationTime = reservationTimeService.createTime(request);

        // then
        assertThat(reservationTime.startAt()).isEqualTo(Fixtures.TIME_10_10);
    }

    @DisplayName("예약 시간 서비스는 중복된 예약 시간 요청이 들어오면 예외가 발생한다.")
    @Test
    void validateIsDuplicated() {
        // given
        Mockito.when(reservationTimeRepository.findAll())
                .thenReturn(List.of(Fixtures.reservationTimeFixture));
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(
                Fixtures.reservationTimeFixture.getStartAt());

        // when & then
        assertThatThrownBy(() -> reservationTimeService.createTime(request))
                .isInstanceOf(IllegalTimeException.class)
                .hasMessage("중복된 예약 시간입니다.");
    }

    @DisplayName("예약 시간 서비스는 id에 맞는 시간을 반환한다.")
    @Test
    void readReservationTime() {
        // given
        Long id = 1L;
        Mockito.when(reservationTimeRepository.findById(id))
                .thenReturn(Optional.of(Fixtures.reservationTimeFixture));

        // when
        ReservationTimeResponse reservationTime = reservationTimeService.readReservationTime(id);

        // then
        assertThat(reservationTime.startAt()).isEqualTo(Fixtures.TIME_10_10);
    }

    @DisplayName("예약 시간 서비스는 시간들을 반환한다.")
    @Test
    void readReservationTimes() {
        // given
        Mockito.when(reservationTimeRepository.findAll())
                .thenReturn(List.of(Fixtures.reservationTimeFixture));

        // when
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.readReservationTimes(
                Fixtures.DATE_AFTER_6_MONTH_LATER, 1L
        );

        // then
        assertThat(reservationTimes).hasSize(1);
    }

    @DisplayName("예약 시간 서비스는 지정된 날짜와 테마별 예약 가능 여부를 포함하여 시간들을 반환한다.")
    @Test
    void readReservationTimesByDateAndThemeId() {
        // given
        LocalDate date = Fixtures.DATE_AFTER_6_MONTH_LATER;
        Long themeId = 1L;
        Mockito.when(reservationRepository.findByDateAndThemeId(date, themeId))
                .thenReturn(List.of(new Reservation(Fixtures.memberFixture, date, Fixtures.reservationTimeFixture, Fixtures.themeFixture)));
        Mockito.when(reservationTimeRepository.findAll())
                .thenReturn(List.of(Fixtures.reservationTimeFixture));

        // when
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.readReservationTimes(date, themeId);

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(reservationTimes).hasSize(1);
        softAssertions.assertThat(reservationTimes).contains(ReservationTimeResponse.of(Fixtures.reservationTimeFixture, true));
        softAssertions.assertAll();
    }

    @DisplayName("예약 시간 서비스는 id에 맞는 시간을 삭제한다.")
    @Test
    void deleteTime() {
        // given
        Long id = 1L;
        Mockito.when(reservationRepository.existsByTimeId(id))
                .thenReturn(false);
        Mockito.doNothing().when(reservationTimeRepository).deleteById(id);

        // when & then
        assertThatCode(() -> reservationTimeService.deleteTime(id))
                .doesNotThrowAnyException();
    }

    @DisplayName("예약 시간 서비스는 시간 정보를 삭제할 때 id에 맞는 시간에 예약이 존재하면 예외가 발생한다.")
    @Test
    void deleteTimeWithExistsReservation() {
        // given
        Long id = 1L;
        Mockito.when(reservationRepository.existsByTimeId(id))
                .thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteTime(id))
                .isInstanceOf(ReferencedReservationExistException.class);
    }
}
