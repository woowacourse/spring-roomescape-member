package roomescape.time.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.time.domain.ReservationTime;
import roomescape.time.dto.request.ReservationTimeSaveDto;
import roomescape.time.dto.response.ReservationTimeDetailDto;
import roomescape.time.repository.FakeReservationTimeRepository;

class ReservationTimeServiceTest {

    private ReservationTimeService reservationTimeService;

    // TODO @BeforeEach 삭제 후 필요한 곳에서 Insert
    @BeforeEach
    void setup() {
        FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
        this.reservationTimeService = new ReservationTimeService(reservationTimeRepository);

        reservationTimeService.register(new ReservationTimeSaveDto(LocalTime.of(15, 40)));
        reservationTimeService.register(new ReservationTimeSaveDto(LocalTime.of(16, 0)));
    }

    @Test
    @DisplayName("모든 예약 시간 정보를 조회한다.")
    void findAll() {
        // when
        List<ReservationTimeDetailDto> actual = reservationTimeService.findAll();

        // then
        assertThat(actual)
                .hasSize(2);
    }

    @Test
    @DisplayName("예약 시간을 추가한다.")
    void register() {
        // given
        reservationTimeService.register(new ReservationTimeSaveDto(LocalTime.of(12, 0)));

        // when & then
        assertThat(reservationTimeService.findAll())
                .hasSize(3);
    }

    @Test
    @DisplayName("예약 시간을 삭제한다.")
    void delete() {
        // given
        ReservationTime reservationTime = reservationTimeService.register(
                new ReservationTimeSaveDto(LocalTime.of(12, 0)));

        // when
        reservationTimeService.delete(reservationTime.id());

        // then
        assertThat(reservationTimeService.findAll())
                .hasSize(2);
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간 삭제 시 예외가 발생한다.")
    void deleteNotExist() {
        // given
        Long wrongId = Long.MIN_VALUE;

        // when & then
        assertThatThrownBy(() -> reservationTimeService.delete(wrongId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 예약 시간입니다.");
    }

    @Test
    @DisplayName("이미 존재하는 예약 시간 생성 시 예외가 발생한다.")
    void register_already_exist() {
        // given
        ReservationTimeSaveDto command = new ReservationTimeSaveDto(LocalTime.of(15, 40));

        // when & then
        assertThatThrownBy(() -> reservationTimeService.register(command))
                .isInstanceOf(ConflictException.class)
                .hasMessage("이미 존재하는 예약 시간입니다.");
    }
}
