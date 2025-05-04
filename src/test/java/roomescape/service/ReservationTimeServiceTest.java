package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static roomescape.test.fixture.DateFixture.NEXT_DAY;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime;
import roomescape.dto.other.TimeWithBookState;
import roomescape.dto.request.ReservationTimeCreationRequest;
import roomescape.exception.BadRequestException;
import roomescape.exception.NotFoundException;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationtime.ReservationTimeRepository;

class ReservationTimeServiceTest {

    private final ReservationRepository reservationRepository = mock(ReservationRepository.class);
    private final ReservationTimeRepository timeRepository = mock(ReservationTimeRepository.class);
    private final ReservationTimeService reservationTimeService =
            new ReservationTimeService(reservationRepository, timeRepository);

    @DisplayName("등록된 모든 예약 가능 시간을 조회활 수 있다")
    @Test
    void canGetReservationTimes() {
        // given
        List<ReservationTime> expectedTimes = List.of(
                new ReservationTime(1L, LocalTime.now()),
                new ReservationTime(2L, LocalTime.now()),
                new ReservationTime(3L, LocalTime.now()));
        when(timeRepository.findAll()).thenReturn(expectedTimes);

        // when
        List<ReservationTime> actualTimes = reservationTimeService.getAllReservationTime();

        // then
        assertThat(actualTimes).containsExactlyElementsOf(expectedTimes);
    }

    @DisplayName("예약 여부와 함께 예약 가능 시간들을 조회할 수 있다")
    @Test
    void canGetAllReservationTimeWithBookState() {
        // given
        List<TimeWithBookState> expectedTimes = List.of(
                new TimeWithBookState(1L, LocalTime.now(), true),
                new TimeWithBookState(2L, LocalTime.now(), false),
                new TimeWithBookState(3L, LocalTime.now(), true));
        when(timeRepository.findAllWithBookState(NEXT_DAY, 1L)).thenReturn(expectedTimes);

        // when
        List<TimeWithBookState> actualTimes = reservationTimeService.getAllReservationTimeWithBookState(NEXT_DAY, 1L);

        // then
        assertThat(actualTimes).containsExactlyElementsOf(expectedTimes);
    }

    @DisplayName("ID를 통해 예약 가능 시간을 조회할 수 있다")
    @Test
    void canGetReservationTimeById() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.now());
        when(timeRepository.findById(1L)).thenReturn(Optional.of(time));

        // when
        ReservationTime actualTime = reservationTimeService.getReservationTimeById(1L);

        // then
        assertThat(actualTime).isEqualTo(time);
    }

    @DisplayName("ID를 통해 예약 가능 ㅅ간을 조회할 때, 데이터가 없으면 예외를 발생시킨다")
    @Test
    void cannotGetReservationTimeByIdWhenEmptyTime() {
        // given
        when(timeRepository.findById(1L)).thenReturn(Optional.empty());

        // when && then
        assertThatThrownBy(() -> reservationTimeService.getReservationTimeById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("[ERROR] ID에 해당하는 예약 시간이 존재하지 않습니다.");
    }

    @DisplayName("예약 가능 시간을 추가할 수 있다")
    @Test
    void canCreateReservationTime() {
        // given
        ReservationTimeCreationRequest request = new ReservationTimeCreationRequest(LocalTime.now());
        when(timeRepository.checkExistenceByStartAt(request.startAt())).thenReturn(false);
        when(timeRepository.add(ReservationTime.createWithoutId(request.startAt()))).thenReturn(1L);

        // when
        long savedId = reservationTimeService.saveReservationTime(request);

        // then
        assertThat(savedId).isEqualTo(1L);
    }

    @DisplayName("예약 가능 시간을 추가할때, 이미 추가한 시간의 경우 추가할 수 없다")
    @Test
    void canCreateSameReservationTime() {
        // given
        ReservationTimeCreationRequest request = new ReservationTimeCreationRequest(LocalTime.now());
        when(timeRepository.checkExistenceByStartAt(request.startAt())).thenReturn(true);
        when(timeRepository.add(ReservationTime.createWithoutId(request.startAt()))).thenReturn(1L);

        // when & then
        assertThatThrownBy(() -> reservationTimeService.saveReservationTime(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[Error] 이미 추가가 완료된 예약 가능 시간입니다.");
    }

    @DisplayName("ID를 통해 예약 가능 시간을 삭제할 수 있다")
    @Test
    void canDeleteReservationTime() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.now());
        when(timeRepository.findById(time.getId())).thenReturn(Optional.of(time));
        when(reservationRepository.checkExistenceInTime(time.getId())).thenReturn(false);

        // when & then
        assertAll(
                () -> assertThatCode(() -> reservationTimeService.deleteReservationTime(time.getId()))
                        .doesNotThrowAnyException(),
                () -> verify(timeRepository, times(1)).deleteById(time.getId())
        );
    }

    @DisplayName("ID를 통해 예약 가능 시간을 삭제할때, 존재하지 않는 예약 가능 시간을 삭제하려고 할 경우 예외를 발생시킨다")
    @Test
    void canNotDeleteWithInvalidId() {
        // given
        long noneExistentId = 1L;
        when(timeRepository.findById(noneExistentId)).thenReturn(Optional.empty());
        when(reservationRepository.checkExistenceInTime(noneExistentId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(noneExistentId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("[ERROR] ID에 해당하는 예약 시간이 존재하지 않습니다.");
    }

    @DisplayName("이미 해당 시간에 예약이 존재하는 경우 예약을 제거할 수 없다")
    @Test
    void canNotDeleteBecauseReservations() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.now());
        when(timeRepository.findById(time.getId())).thenReturn(Optional.of(time));
        when(reservationRepository.checkExistenceInTime(time.getId())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(time.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 이미 해당 시간에 대한 예약 데이터들이 존재합니다.");
    }
}
