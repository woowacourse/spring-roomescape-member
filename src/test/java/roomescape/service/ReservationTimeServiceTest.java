package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.dto.ReservationTimeCreateRequestDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.repository.FakeReservationTimeRepository;
import roomescape.repository.ReservationTimeRepository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ReservationTimeServiceTest {

    ReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository(new ArrayList<>());
    ReservationTimeService reservationTimeService = new ReservationTimeService(reservationTimeRepository);

    @DisplayName("ReservationTime을 생성할 수 있다")
    @Test
    void createReservationTimeTest() {
        ReservationTimeCreateRequestDto requestDto = new ReservationTimeCreateRequestDto(LocalTime.of(10, 0));
        ReservationTimeResponseDto responseDto = reservationTimeService.createReservationTime(requestDto);

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @DisplayName("모든 ReservationTime을 조회할 수 있다")
    @Test
    void findAllReservationTimesTest() {
        reservationTimeService.createReservationTime(new ReservationTimeCreateRequestDto(LocalTime.of(10, 0)));
        reservationTimeService.createReservationTime(new ReservationTimeCreateRequestDto(LocalTime.of(11, 0)));

        List<ReservationTimeResponseDto> allTimes = reservationTimeService.findAllReservationTimes();

        assertThat(allTimes).hasSize(2);
        assertThat(allTimes).extracting("startAt").containsExactly(LocalTime.of(10, 0), LocalTime.of(11, 0));
    }


    @Nested
    @DisplayName("예약시간 삭제")
    class ReservationTimeDeleteTest {

        @DisplayName("ReservationTime을 삭제할 수 있다")
        @Test
        void deleteReservationTimeByIdTest() {
            ReservationTimeCreateRequestDto requestDto = new ReservationTimeCreateRequestDto(LocalTime.of(10, 0));
            ReservationTimeResponseDto responseDto = reservationTimeService.createReservationTime(requestDto);

            reservationTimeService.deleteReservationTimeById(responseDto.id());

            List<ReservationTimeResponseDto> allTimes = reservationTimeService.findAllReservationTimes();
            assertThat(allTimes).isEmpty();
        }

        @DisplayName("존재하지 않는 ReservationTime을 삭제하려고 하면 예외가 발생한다")
        @Test
        void deleteNonExistentReservationTimeTest() {
            Long nonExistentId = 2L;

            assertThatThrownBy(() -> reservationTimeService.deleteReservationTimeById(nonExistentId))
                    .isInstanceOf(IllegalStateException.class);
        }
    }
}
