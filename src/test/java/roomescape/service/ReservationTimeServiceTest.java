package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.exception.EntityNotFoundException;
import roomescape.repository.ReservationDao;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeServiceTest {

    private ReservationDao reservationDao;
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        reservationDao = new ReservationInMemoryDao();
        reservationTimeService = new ReservationTimeService(reservationDao, new ReservationTimeInMemoryDao());
    }

    @Test
    void 예약_시간을_추가한다() {
        // given
        LocalTime startAt = LocalTime.of(10, 0);
        ReservationTimeRequest request = new ReservationTimeRequest(startAt);

        // when
        ReservationTimeResponse response = reservationTimeService.add(request);

        // then
        ReservationTimeResponse expected = new ReservationTimeResponse(1L, startAt);
        assertThat(response).isEqualTo(expected);
    }

    @Test
    void 모든_예약_시간을_조회한다() {
        // given
        LocalTime tenHour = LocalTime.of(10, 0);
        LocalTime elevenHour = LocalTime.of(11, 0);
        reservationTimeService.add(new ReservationTimeRequest(tenHour));
        reservationTimeService.add(new ReservationTimeRequest(elevenHour));

        // when
        List<ReservationTimeResponse> all = reservationTimeService.findAll();

        // then
        assertThat(all).containsExactly(
            new ReservationTimeResponse(1L, tenHour),
            new ReservationTimeResponse(2L, elevenHour));
    }

    @Test
    void 예약_시간을_삭제한다() {
        // given
        LocalTime tenHour = LocalTime.of(10, 0);
        ReservationTimeResponse addedReservationTime
            = reservationTimeService.add(new ReservationTimeRequest(tenHour));

        // when
        reservationTimeService.deleteById(addedReservationTime.id());

        // then
        List<ReservationTimeResponse> all = reservationTimeService.findAll();
        assertThat(all.isEmpty()).isTrue();
    }

    @Test
    void 없는_예약시간을_삭제하면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteById(9L))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("삭제할 예약시간이 없습니다.");
    }
    // TODO : 예약이 존재할때, 예약시간 삭제 검증 테스트 작성
}