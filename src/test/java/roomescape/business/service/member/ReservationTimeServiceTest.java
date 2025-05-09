package roomescape.business.service.member;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.business.domain.reservation.ReservationTime;
import roomescape.persistence.ReservationTimeRepository;
import roomescape.persistence.fakerepository.FakeReservationTimeRepository;
import roomescape.presentation.admin.dto.ReservationTimeResponseDto;

class ReservationTimeServiceTest {

    private ReservationTimeService reservationTimeService;
    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void setUp() {
        reservationTimeRepository = new FakeReservationTimeRepository();
        reservationTimeService = new ReservationTimeService(reservationTimeRepository);
    }

    @DisplayName("예약 가능한 시간 목록을 조회한다")
    @Test
    void getAllTimes() {
        // given
        reservationTimeRepository.add(new ReservationTime(LocalTime.parse("10:00")));
        reservationTimeRepository.add(new ReservationTime(LocalTime.parse("11:00")));
        reservationTimeRepository.add(new ReservationTime(LocalTime.parse("12:00")));

        // when
        var times = reservationTimeService.getAllTimes();

        // then
        assertThat(times)
                .hasSize(3)
                .containsExactly(
                        new ReservationTimeResponseDto(1L, LocalTime.parse("10:00")),
                        new ReservationTimeResponseDto(2L, LocalTime.parse("11:00")),
                        new ReservationTimeResponseDto(3L, LocalTime.parse("12:00"))
                );
    }

    @DisplayName("아이디로 예약 가능한 시간을 조회한다")
    @Test
    void getTimeById() {
        // given
        LocalTime time = LocalTime.parse("10:00");
        Long timeId = reservationTimeRepository.add(new ReservationTime(time)).getId();
        ReservationTimeResponseDto expected = new ReservationTimeResponseDto(timeId, time);

        // when
        ReservationTimeResponseDto actual = reservationTimeService.getTimeById(timeId);

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }
}
