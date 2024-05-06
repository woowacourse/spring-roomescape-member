package roomescape.reservationtime.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.fixture.Fixture;
import roomescape.reservation.fake.FakeReservationRepository;
import roomescape.reservationtime.dto.request.CreateReservationTimeRequest;
import roomescape.reservationtime.dto.response.CreateReservationTimeResponse;
import roomescape.reservationtime.dto.response.FindReservationTimeResponse;
import roomescape.reservationtime.fake.FakeReservationTimeRepository;

class ReservationTimeServiceTest {

    private static ReservationTimeService reservationTimeService;
    private static FakeReservationTimeRepository reservationTimeRepository;
    private static FakeReservationRepository reservationRepository;

    @BeforeAll
    static void beforeAll() {
        reservationTimeRepository = new FakeReservationTimeRepository();
        reservationRepository = new FakeReservationRepository();
        reservationTimeService = new ReservationTimeService(reservationTimeRepository, reservationRepository);
    }

    @BeforeEach
    void setUp() {
        reservationRepository.clear();
        reservationTimeRepository.clear();

        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_2);
        reservationRepository.save(Fixture.RESERVATION_1);
    }

    @Test
    @DisplayName("예약 시간 생성 시 해당 값을 반환한다.")
    void createReservationTime() {
        // given
        var request = new CreateReservationTimeRequest(
                LocalTime.of(11, 11));

        // when
        CreateReservationTimeResponse reservationTime = reservationTimeService.createReservationTime(request);

        // then
        assertThat(reservationTime.id()).isEqualTo(3L);
        assertThat(reservationTime.startAt()).isEqualTo("11:11");
    }

    @Test
    @DisplayName("예약 시간 목록 조회 시 저장된 예약 시간에 대한 정보를 반환한다.")
    void getReservationTimes() {
        // when & then
        assertThat(reservationTimeService.getReservationTimes())
                .containsExactly(
                        FindReservationTimeResponse.of(Fixture.RESERVATION_TIME_1),
                        FindReservationTimeResponse.of(Fixture.RESERVATION_TIME_2));
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약 시간에 대한 정보를 반환한다.")
    void getReservationTime() {
        // when & then
        assertThat(reservationTimeService.getReservationTime(1L))
                .isEqualTo(FindReservationTimeResponse.of(Fixture.RESERVATION_TIME_1));
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약 시간이 없는 경우 예외가 발생한다.")
    void getReservationTime_ifNotExist_throwException() {
        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteById(99999L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 예약 시간이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약 시간을 삭제한다.")
    void deleteById() {
        // when
        reservationTimeService.deleteById(2L);

        // then
        assertThat(reservationRepository.findById(2L))
                .isEmpty();
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약 시간이 없는 경우 예외가 발생한다.")
    void deleteById_ifNotExist_throwException() {
        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteById(99999L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 예약 시간이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("해당하는 시간을 사용 중인 예약이 존재할 경우 예외가 발생한다.")
    void deleteById_ifAlreadyUsed_throwException() {
        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteById(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("시간을 사용 중인 예약이 존재합니다.");
    }
}
