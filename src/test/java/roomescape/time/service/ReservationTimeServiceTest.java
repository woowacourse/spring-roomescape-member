package roomescape.time.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.time.fixture.ReservationTimeFixture.saveDto;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.time.domain.ReservationTime;
import roomescape.time.dto.request.ReservationTimeSaveDto;
import roomescape.time.fixture.FakeReservationTimeRepository;
import roomescape.time.fixture.ReservationTimeFixture;

class ReservationTimeServiceTest {

    private FakeReservationTimeRepository reservationTimeRepository;
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setup() {
        reservationTimeRepository = new FakeReservationTimeRepository();
        reservationTimeService = new ReservationTimeService(reservationTimeRepository);
    }

    @Test
    @DisplayName("모든 예약 시간 정보를 조회한다.")
    void findAll() {
        // given
        List<ReservationTime> times = List.of(
                ReservationTimeFixture.time15(),
                ReservationTimeFixture.time16()
        );
        saveAll(times);

        // when
        List<ReservationTime> actual = reservationTimeService.findAll();

        // then
        assertThat(actual)
                .hasSize(times.size());
    }

    @Test
    @DisplayName("예약 시간을 추가한다.")
    void register() {
        // given
        List<ReservationTime> times = List.of();

        // when
        reservationTimeService.register(saveDto(LocalTime.of(12, 0)));

        // then
        assertThat(reservationTimeService.findAll())
                .hasSize(times.size() + 1);
    }

    @Test
    @DisplayName("예약 시간을 삭제한다.")
    void delete() {
        // given
        List<ReservationTime> times = List.of(
                ReservationTimeFixture.time15(),
                ReservationTimeFixture.time16()
        );
        List<ReservationTime> savedTimes = saveAll(times);

        // when
        reservationTimeService.delete(savedTimes.getFirst().id());

        // then
        assertThat(reservationTimeService.findAll())
                .hasSize(times.size() - 1);
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간 삭제 시 예외가 발생한다.")
    void deleteNotExist() {
        // given
        Long wrongId = Long.MIN_VALUE;

        // when & then
        assertThatThrownBy(() -> reservationTimeService.delete(wrongId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 예약 시간입니다.");
    }

    @Test
    @DisplayName("이미 존재하는 예약 시간 생성 시 예외가 발생한다.")
    void register_already_exist() {
        // given
        ReservationTime saved = save(ReservationTimeFixture.time15());
        ReservationTimeSaveDto duplicatedCommand = saveDto(saved.startAt());

        // when & then
        assertThatThrownBy(() -> reservationTimeService.register(duplicatedCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 예약 시간입니다.");
    }

    private List<ReservationTime> saveAll(List<ReservationTime> reservationTimes) {
        List<ReservationTime> savedReservationTimes = new ArrayList<>();
        for (ReservationTime reservationTime : reservationTimes) {
            savedReservationTimes.add(save(reservationTime));
        }
        return savedReservationTimes;
    }

    private ReservationTime save(ReservationTime reservationTime) {
        return reservationTimeRepository.save(reservationTime);
    }

}
