package roomescape.reservationtime.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import roomescape.reservation.repository.FakeReservationRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.dto.request.CreateReservationTimeRequest;
import roomescape.reservationtime.dto.response.CreateReservationTimeResponse;
import roomescape.reservationtime.dto.response.FindReservationTimeResponse;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.reservationtime.repository.FakeReservationTimeRepository;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.util.DummyDataFixture;

class ReservationTimeServiceTest extends DummyDataFixture {
    private ReservationTimeService reservationTimeService;

    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();

        reservationTimeService = new ReservationTimeService(reservationTimeRepository, reservationRepository);
    }

    @Nested
    class createReservationTime {
        @Test
        @DisplayName("예약 시간 생성 시 해당 데이터의 id값을 반환한다.")
        void createReservationTime() {
            // given
            CreateReservationTimeRequest createReservationTimeRequest = new CreateReservationTimeRequest(
                    LocalTime.of(11, 11));

            // stub
            Mockito.when(reservationTimeRepository.save(any(ReservationTime.class))).thenReturn(getReservationTimeById(1L));

            // when & then
            assertThat(reservationTimeService.createReservationTime(createReservationTimeRequest))
                    .isEqualTo(CreateReservationTimeResponse.from(getReservationTimeById(1L)));
        }

        @Test
        @DisplayName("예약 시간 생성 시 이미 존재하는 시간인 경우 예외를 반환한다.")
        void createReservationTime_WhenAlreadyExistsTime() {
            // given
            CreateReservationTimeRequest createReservationTimeRequest = new CreateReservationTimeRequest(
                    LocalTime.of(10, 00));

            // stub
            Mockito.when(reservationTimeRepository.save(any(ReservationTime.class))).thenReturn(getReservationTimeById(1L));

            // when & then
            assertThat(reservationTimeService.createReservationTime(createReservationTimeRequest))
                    .isEqualTo(CreateReservationTimeResponse.from(getReservationTimeById(1L)));
        }

    }

    @Test
    @DisplayName("예약 시간 목록 조회 시 저장된 예약 시간에 대한 정보를 반환한다.")
    void getReservationTimes() {
        // stub
        Mockito.when(reservationTimeRepository.findAll())
                .thenReturn(super.getPreparedReservationTimes());

        // when & then
        assertThat(reservationTimeService.getReservationTimes()).containsExactly(
                new FindReservationTimeResponse(1L, LocalTime.parse("10:00")),
                new FindReservationTimeResponse(2L, LocalTime.parse("12:00")),
                new FindReservationTimeResponse(3L, LocalTime.parse("14:00")),
                new FindReservationTimeResponse(4L, LocalTime.parse("16:00")),
                new FindReservationTimeResponse(5L, LocalTime.parse("18:00")),
                new FindReservationTimeResponse(6L, LocalTime.parse("20:00"))
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
                new FindReservationTimeResponse(timeId, LocalTime.parse("10:00")));
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
                .hasMessage("삭제하려는 예약 시간이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약 시간을 삭제한다.")
    void deleteById() {
        // given
        long timeId = 7L;

        // stub
        Mockito.when(reservationTimeRepository.existsById(timeId)).thenReturn(true);
        Mockito.when(reservationRepository.existsByTimeId(timeId)).thenReturn(false);

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
                .hasMessage("삭제하려는 예약 시간이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("해당하는 시간을 사용 중인 예약이 존재할 경우 예외가 발생한다.")
    void deleteById_ifAlreadyUsed_throwException() {
        // given
        long timeId = 2L;

        // stub
        Mockito.when(reservationTimeRepository.existsById(timeId))
                .thenReturn(true);

        Mockito.when(reservationRepository.existsByTimeId(timeId))
                .thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteById(timeId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("삭제하려는 시간을 사용 중인 예약이 존재합니다.");
    }
}
