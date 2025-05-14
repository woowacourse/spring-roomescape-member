package roomescape.service;

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
import roomescape.domain.ReservationTime;
import roomescape.exception.custom.BusinessRuleViolationException;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.NotFoundValueException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.request.ReservationTimeCreation;
import roomescape.service.dto.response.AvailableReservationTimeResult;
import roomescape.service.dto.response.ReservationTimeResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    ReservationTimeRepository reservationTimeRepository;

    @InjectMocks
    ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("시간 데이터를 추가할 수 있어야 한다")
    void addReservationTime() {
        //given //when
        when(reservationTimeRepository.existsByStartAt(any(LocalTime.class)))
                .thenReturn(false);
        when(reservationTimeRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(new ReservationTime(1L, LocalTime.of(15, 0))));

        ReservationTimeCreation creation = new ReservationTimeCreation(LocalTime.of(15, 0));
        ReservationTimeResult actual = reservationTimeService.addReservationTime(creation);

        //then
        assertThat(actual.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("이미 존재하는 시간 데이터일 경우 예외를 던진다")
    void cannotAddReservationTime() {
        //given
        when(reservationTimeRepository.existsByStartAt(any(LocalTime.class)))
                .thenReturn(true);
        ReservationTimeCreation creation = new ReservationTimeCreation(LocalTime.of(10, 0));

        //when & then
        assertThatThrownBy(() -> reservationTimeService.addReservationTime(creation))
                .isInstanceOf(ExistedDuplicateValueException.class)
                .hasMessage("이미 존재하는 예약 가능 시간입니다: %s".formatted(creation.startAt()));
    }

    @Test
    @DisplayName("시간 데이터를 조회할 수 있어야 한다")
    void findAllReservationTimes() {
        //given
        when(reservationTimeRepository.findAll())
                .thenReturn(List.of(new ReservationTime(1L, LocalTime.of(10, 0))));

        // when
        List<ReservationTimeResult> actual = reservationTimeService.findAllReservationTimes();

        //then
        assertThat(actual).hasSize(1);
    }

    @Test
    @DisplayName("이용가능한 예약 시간을 조회한다")
    void findAvailableTime() {
        //given
        when(reservationTimeRepository.findAll())
                .thenReturn(List.of(new ReservationTime(1L, LocalTime.of(10, 0)),
                        new ReservationTime(2L, LocalTime.of(11, 0))));
        when(reservationTimeRepository.findAllBookedTime(any(LocalDate.class), any(Long.class)))
                .thenReturn(List.of(new ReservationTime(1L, LocalTime.of(10, 0))));

        LocalDate date = LocalDate.of(2025, 1, 1);
        long themeId = 1L;

        // when
        List<AvailableReservationTimeResult> actual = reservationTimeService.findAllAvailableTime(date, themeId);

        //then
        assertThat(actual.stream().filter(AvailableReservationTimeResult::alreadyBooked)).isNotEmpty();
        assertThat(actual.stream().filter(time -> !time.alreadyBooked())).isNotEmpty();
    }

    @Test
    @DisplayName("id를 기반으로 시간 데이터를 삭제할 수 있다")
    void deleteById() {
        //given
        when(reservationTimeRepository.deleteById(any(Long.class)))
                .thenReturn(true);

        //when //then
        assertThatCode(() -> reservationTimeService.deleteById(2L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("존재하지 않는 시간을 삭제하는 경우 예외를 던진다")
    void deleteNotExistTimeById() {
        //given
        when(reservationTimeRepository.deleteById(any(Long.class)))
                .thenReturn(false);
        
        // when //then
        assertThatThrownBy(() -> reservationTimeService.deleteById(1000L))
                .isInstanceOf(NotFoundValueException.class)
                .hasMessageContaining("존재하지 않는 예약 시간입니다");
    }

    @Test
    @DisplayName("사용중인 예약 시간을 삭제하려는 경우 예외를 던진다")
    void deleteUsedTimeById() {
        //given //when //then
        assertThatThrownBy(() -> reservationTimeService.deleteById(1L))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("사용 중인 예약 시간입니다");
    }
}
