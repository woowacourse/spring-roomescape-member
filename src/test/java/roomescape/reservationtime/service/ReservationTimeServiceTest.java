package roomescape.reservationtime.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.reservationtime.dto.request.CreateReservationTimeRequest;
import roomescape.reservationtime.dto.response.CreateReservationTimeResponse;
import roomescape.reservationtime.dto.response.FindReservationTimeResponse;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.util.DummyDataFixture;

@SpringBootTest
class ReservationTimeServiceTest extends DummyDataFixture {

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    @DisplayName("예약 시간 생성 시 해당 데이터의 id값을 반환한다.")
    void createReservationTime() {
        // given
        CreateReservationTimeRequest createReservationTimeRequest = new CreateReservationTimeRequest(
                LocalTime.of(11, 11));

        // stub // TODO:반환 타입 지정
        Mockito.when(reservationTimeRepository.save(any(ReservationTime.class))).thenReturn(any(ReservationTime.class));

        // when
        CreateReservationTimeResponse reservationTime = reservationTimeService.createReservationTime(
                createReservationTimeRequest);

        // then
        assertThat(reservationTime.id()).isEqualTo(10L);
    }

    @Test
    @DisplayName("예약 시간 목록 조회 시 저장된 예약 시간에 대한 정보를 반환한다.")
    void getReservationTimes() {
        // stub
        Mockito.when(reservationTimeRepository.findAll())
                .thenReturn(super.getPreParedReservationTimes());

        // when & then
        assertThat(reservationTimeService.getReservationTimes()).containsExactly(
                new FindReservationTimeResponse(1L, "15:40"),
                new FindReservationTimeResponse(2L, "10:00"),
                new FindReservationTimeResponse(3L, "13:00")
        );
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약 시간에 대한 정보를 반환한다.")
    void getReservationTime() {
        // given
        long timeId = 1L;

        // stub
        Mockito.when(reservationTimeRepository.findById(timeId))
                .thenReturn(Optional.ofNullable(super.getReservationTimeById(timeId)));

        // when & then
        assertThat(reservationTimeService.getReservationTime(timeId)).isEqualTo(
                new FindReservationTimeResponse(timeId, "15:40"));
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약 시간이 없는 경우 예외가 발생한다.")
    void getReservationTime_ifNotExist_throwException() {
        // given
        long timeId = 1L;

        // stub
        Mockito.when(reservationTimeRepository.findById(timeId))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteById(timeId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 예약 시간이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약 시간을 삭제한다.")
    void deleteById() {
        // given
        long timeId = 1L;

        // stub
        Mockito.when(reservationTimeRepository.findById(timeId))
                .thenReturn(Optional.of(new ReservationTime(null, any(LocalTime.class))));

        // when
        reservationTimeService.deleteById(timeId);

        // then
        Mockito.verify(reservationTimeRepository, Mockito.times(1)).deleteById(timeId);
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약 시간이 없는 경우 예외가 발생한다.")
    void deleteById_ifNotExist_throwException() {
        // given
        long timeId = 2L;

        // stub
        Mockito.when(reservationTimeRepository.findById(timeId))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteById(timeId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 예약 시간이 존재하지 않습니다.");
    }
}
