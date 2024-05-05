package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.application.dto.ReservationTimeRequest;
import roomescape.application.dto.ReservationTimeResponse;
import roomescape.domain.PlayerName;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationCommandRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeName;
import roomescape.domain.ThemeRepository;

@ServiceTest
class ReservationTimeServiceTest {
    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationCommandRepository reservationCommandRepository;

    @DisplayName("예약 시간을 생성한다.")
    @Test
    void shouldReturnReservationTimeResponseWhenCreateReservationTime() {
        LocalTime startAt = LocalTime.of(10, 0);
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(startAt);
        reservationTimeService.create(reservationTimeRequest);
        List<ReservationTime> times = reservationTimeRepository.findAll();
        assertThat(times).hasSize(1);
    }

    @DisplayName("이미 존재하는 예약 시간을 생성 요청하면 예외가 발생한다.")
    @Test
    void shouldThrowsIllegalStateExceptionWhenCreateExistStartAtTime() {
        LocalTime startAt = createTime(10, 0).getStartAt();
        ReservationTimeRequest request = new ReservationTimeRequest(startAt);
        assertThatCode(() -> reservationTimeService.create(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(String.format("이미 존재하는 예약시간이 있습니다. 해당 시간:%s", startAt));
    }

    @DisplayName("예약 시간 조회를 요청하면 저장되어있는 모든 예약 시간대를 반환한다.")
    @Test
    void shouldReturnAllReservationTimesWhenFindAll() {
        ReservationTime time = createTime(10, 0);
        List<ReservationTimeResponse> times = reservationTimeService.findAll();
        assertThat(times).containsExactly(
                new ReservationTimeResponse(time.getId(), LocalTime.of(10, 0))
        );
    }

    @DisplayName("예약 삭제 요청을 하면, 해당 예약이 저장되어있는지 확인 후 존재하면 삭제한다.")
    @Test
    void shouldDeleteReservationWhenDeleteById() {
        ReservationTime reservationTime = createTime(10, 0);
        reservationTimeService.deleteById(reservationTime.getId());
        assertThat(reservationTimeRepository.findAll()).isEmpty();
    }

    @DisplayName("예약에 사용된 예약 시간을 삭제 요청하면, IllegalStateException 예외가 발생한다.")
    @Test
    void shouldThrowsExceptionReservationWhenReservedInTime() {
        ReservationTime reservationTime = createTime(10, 0);
        Theme theme = new Theme(1L, new ThemeName("test"), "description", "url");
        Theme savedTheme = themeRepository.create(theme);
        reservationCommandRepository.create(
                new Reservation(
                        new PlayerName("test"),
                        LocalDate.of(2024, 12, 25),
                        reservationTime,
                        savedTheme)
        );

        assertThatCode(() -> reservationTimeService.deleteById(reservationTime.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageStartingWith("해당 예약 시간에 연관된 예약이 존재하여 삭제할 수 없습니다. 삭제 요청한 시간");
    }

    @DisplayName("존재하지 않는 예약 시간을 삭제 요청하면, IllegalArgumentException 예외가 발생한다.")
    @Test
    void shouldThrowsIllegalArgumentExceptionWhenReservationTimeDoesNotExist() {
        assertThatCode(() -> reservationTimeService.deleteById(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 예약 시간 입니다.");
    }

    private ReservationTime createTime(int hour, int minute) {
        LocalTime startAt = LocalTime.of(hour, minute);
        return reservationTimeRepository.create(new ReservationTime(startAt));
    }
}
