package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.dto.ReservationTimeRequestDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.exception.EntityNotFoundException;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeServiceTest {

    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        reservationTimeService = new ReservationTimeService(new ReservationTimeInMemoryDao());
    }

    @Test
    void 예약_시간을_추가한다() {
        // given
        LocalTime startAt = LocalTime.of(10, 0);
        ReservationTimeRequestDto request = new ReservationTimeRequestDto(startAt);

        // when
        ReservationTimeResponseDto response = reservationTimeService.add(request);

        // then
        ReservationTimeResponseDto expected = new ReservationTimeResponseDto(1L, startAt);
        assertThat(response).isEqualTo(expected);
    }

    @Test
    void 모든_예약_시간을_조회한다() {
        // given
        LocalTime tenHour = LocalTime.of(10, 0);
        LocalTime elevenHour = LocalTime.of(11, 0);
        reservationTimeService.add(new ReservationTimeRequestDto(tenHour));
        reservationTimeService.add(new ReservationTimeRequestDto(elevenHour));

        // when
        List<ReservationTimeResponseDto> all = reservationTimeService.findAll();

        // then
        assertThat(all).containsExactly(
            new ReservationTimeResponseDto(1L, tenHour),
            new ReservationTimeResponseDto(2L, elevenHour));
    }

    @Test
    void 예약_시간을_삭제한다() {
        // given
        LocalTime tenHour = LocalTime.of(10, 0);
        ReservationTimeResponseDto addedReservationTime
            = reservationTimeService.add(new ReservationTimeRequestDto(tenHour));

        // when
        reservationTimeService.deleteById(addedReservationTime.id());

        // then
        List<ReservationTimeResponseDto> all = reservationTimeService.findAll();
        assertThat(all.isEmpty()).isTrue();
    }

    @Test
    void 없는_예약시간을_삭제하면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteById(9L))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("삭제할 예약시간이 없습니다.");
    }
}