package roomescape.reservationtime.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.reservation.repository.FakeReservationRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.dto.request.CreateReservationTimeRequest;
import roomescape.reservationtime.dto.response.CreateReservationTimeResponse;
import roomescape.reservationtime.dto.response.FindReservationTimeResponse;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.reservationtime.repository.FakeReservationTimeRepository;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.fixture.ReservationFixture;
import roomescape.fixture.ReservationTimeFixture;
import roomescape.fixture.ThemeFixture;

class ReservationTimeServiceTest {
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
        @DisplayName("예약 시간 생성 시 해당 데이터의 정보를 반환한다.")
        void createReservationTime() {
            // given
            CreateReservationTimeRequest createReservationTimeRequest = new CreateReservationTimeRequest(
                    LocalTime.of(11, 11));

            // when
            CreateReservationTimeResponse createReservationTimeResponse =
                    reservationTimeService.createReservationTime(createReservationTimeRequest);

            // then
            assertThat(createReservationTimeResponse.id()).isEqualTo(1L);
        }

        @Test
        @DisplayName("예약 시간 생성 시 이미 존재하는 시간인 경우 예외를 반환한다.")
        void createReservationTime_WhenAlreadyExistsTime() {
            // given
            ReservationTime savedTime = reservationTimeRepository.save(ReservationTimeFixture.getOne());

            CreateReservationTimeRequest createReservationTimeRequest = new CreateReservationTimeRequest(
                    savedTime.getTime());

            // when & then
            assertThatThrownBy(() -> reservationTimeService.createReservationTime(createReservationTimeRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("생성하려는 시간이 이미 존재합니다.");
        }
    }

    @Test
    @DisplayName("예약 시간 목록 조회 시 저장된 예약 시간에 대한 정보를 반환한다.")
    void getReservationTimes() {
        // given
        List<ReservationTime> reservationTimes = ReservationTimeFixture.get(2);
        ReservationTime reservationTime1 = reservationTimeRepository.save(reservationTimes.get(0));
        ReservationTime reservationTime2 = reservationTimeRepository.save(reservationTimes.get(1));

        // when & then
        assertThat(reservationTimeService.getReservationTimes()).containsExactly(
                FindReservationTimeResponse.from(reservationTime1),
                FindReservationTimeResponse.from(reservationTime2)
        );
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약 시간에 대한 정보를 반환한다.")
    void getReservationTime() {
        // given
        ReservationTime savedTime = reservationTimeRepository.save(ReservationTimeFixture.getOne());

        // when & then
        assertThat(reservationTimeService.getReservationTime(savedTime.getId())).isEqualTo(
                FindReservationTimeResponse.from(savedTime));
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약 시간이 없는 경우 예외가 발생한다.")
    void getReservationTime_ifNotExist_throwException() {
        // given
        Long timeId = 1L;

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteById(timeId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("삭제하려는 예약 시간이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약 시간을 삭제한다.")
    void deleteById() {
        // given
        ReservationTime savedTime = reservationTimeRepository.save(ReservationTimeFixture.getOne());

        // when
        reservationTimeService.deleteById(savedTime.getId());

        // then
        assertThat(reservationTimeRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약 시간이 없는 경우 예외가 발생한다.")
    void deleteById_ifNotExist_throwException() {
        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteById(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("삭제하려는 예약 시간이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("해당하는 시간을 사용 중인 예약이 존재할 경우 예외가 발생한다.")
    void deleteById_ifAlreadyUsed_throwException() {
        // given
        ReservationTime savedTime = reservationTimeRepository.save(ReservationTimeFixture.getOne());
        reservationRepository.save(ReservationFixture.getOneWithTimeTheme(savedTime, ThemeFixture.getOne()));

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteById(savedTime.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("삭제하려는 시간을 사용 중인 예약이 존재합니다.");
    }
}
