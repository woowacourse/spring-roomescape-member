package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.application.dto.request.ReservationTimeRequest;
import roomescape.application.dto.response.ReservationTimeResponse;
import roomescape.domain.PlayerName;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeName;
import roomescape.domain.ThemeRepository;
import roomescape.exception.RoomescapeException;

@ServiceTest
class ReservationTimeServiceTest {
    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @DisplayName("예약 시간을 생성한다.")
    @Test
    void shouldReturnReservationTimeResponseWhenCreateReservationTime() {
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.parse("10:00"));
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
                .isInstanceOf(RoomescapeException.class)
                .hasMessage("이미 존재하는 예약입니다.");
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

    @DisplayName("예약에 사용된 예약 시간을 삭제 요청하면, 예외가 발생한다.")
    @Test
    void shouldThrowsExceptionReservationWhenReservedInTime() {
        ReservationTime reservationTime = createTime(10, 0);
        Theme theme = themeRepository.create(new Theme(new ThemeName("테마1"), "테마1 설명", "url"));
        Reservation reservation = reservationRepository.create(new Reservation(
                new PlayerName("오리"), LocalDate.parse("2024-01-01"), reservationTime, theme
        ));
        assertThatCode(() -> reservationTimeService.deleteById(reservationTime.getId()))
                .isInstanceOf(RoomescapeException.class)
                .hasMessage("연관된 예약이 존재하여 삭제할 수 없습니다.");
    }

    @DisplayName("존재하지 않는 예약 시간을 삭제 요청하면, 예외가 발생한다.")
    @Test
    void shouldThrowsIllegalArgumentExceptionWhenReservationTimeDoesNotExist() {
        assertThatCode(() -> reservationTimeService.deleteById(99L))
                .isInstanceOf(RoomescapeException.class)
                .hasMessage("존재하지 않는 예약 시간입니다.");
    }

    private ReservationTime createTime(int hour, int minute) {
        LocalTime startAt = LocalTime.of(hour, minute);
        return reservationTimeRepository.create(new ReservationTime(startAt));
    }
}
