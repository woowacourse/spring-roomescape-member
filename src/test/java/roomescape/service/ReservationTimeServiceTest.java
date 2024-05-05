package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.TestFixtures;
import roomescape.controller.response.AvailableReservationTimeResponse;
import roomescape.controller.response.ReservationTimeResponse;

class ReservationTimeServiceTest {
    private final FakeReservationTimeRepository fakeReservationTimeRepository = new FakeReservationTimeRepository();
    private final FakeReservationRepository fakeReservationRepository = new FakeReservationRepository();
    private final ReservationTimeService reservationTimeService = new ReservationTimeService(
            fakeReservationTimeRepository, fakeReservationRepository
    );


    @BeforeEach
    void setUp() {
        fakeReservationRepository.deleteAll();
        fakeReservationTimeRepository.deleteAll();
        fakeReservationTimeRepository.save(TestFixtures.TIME_1);
        fakeReservationTimeRepository.save(TestFixtures.TIME_2);
        fakeReservationRepository.save(TestFixtures.RESERVATION_1);
    }

    @DisplayName("전체 예약 시간을 반환한다")
    @Test
    void findAll() {
        List<ReservationTimeResponse> reservationTimeResponses = reservationTimeService.findAll();

        assertThat(reservationTimeResponses).isEqualTo(List.of(TestFixtures.TIME_RESPONSE_1, TestFixtures.TIME_RESPONSE_2));
    }

    @DisplayName("해당 테마의 예약 가능한 시간을 반환한다")
    @Test
    void findAllWithAvailability() {
        List<AvailableReservationTimeResponse> allWithAvailability = reservationTimeService.findAllWithAvailability(
                LocalDate.now().plusDays(5), 1L);

        List<AvailableReservationTimeResponse> expectedTimes = List.of(
                AvailableReservationTimeResponse.from(TestFixtures.TIME_1, true),
                AvailableReservationTimeResponse.from(TestFixtures.TIME_2, false)
        );

        assertThat(allWithAvailability).isEqualTo(expectedTimes);
    }

    @DisplayName("예약 시간을 저장한다")
    @Test
    void save() {
        reservationTimeService.save(TestFixtures.TIME_REQUEST_3);

        assertThat(reservationTimeService.findAll()).isEqualTo(List.of(TestFixtures.TIME_RESPONSE_1, TestFixtures.TIME_RESPONSE_2, TestFixtures.TIME_RESPONSE_3));
    }

    @DisplayName("이미 저장된 예약 시간을 저장할 시 예외를 발생시킨다")
    @Test
    void saveDuplicateReservationTime() {
        assertThatThrownBy(() -> reservationTimeService.save(TestFixtures.TIME_REQUEST_2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 예약 시간이 존재합니다. 입력한 시간: " + TestFixtures.TIME_REQUEST_2.startAt());
    }

    @DisplayName("해당 id의 예약 시간을 삭제한다")
    @Test
    void deleteById() {
        reservationTimeService.deleteById(1L);

        assertThat(reservationTimeService.findAll()).isEqualTo(List.of(TestFixtures.TIME_RESPONSE_2));
    }
}
