package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.NotFoundException;
import roomescape.service.dto.request.ReservationTimeCreateRequest;
import roomescape.service.dto.response.ReservationTimeResponse;
import roomescape.service.support.FakeReservationTimeRepository;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeServiceTest {

    private FakeReservationTimeRepository reservationTimeRepository;
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        reservationTimeRepository = new FakeReservationTimeRepository();
        reservationTimeService = new ReservationTimeService(reservationTimeRepository);
    }

    @Test
    void 예약_시간을_생성한다() {
        // when
        ReservationTimeResponse response = reservationTimeService.create(
                new ReservationTimeCreateRequest(
                        LocalTime.of(10, 0),
                        LocalTime.of(10, 30)
                )
        );

        // then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.startAt()).isEqualTo(LocalTime.of(10, 0));
        assertThat(reservationTimeRepository.savedTime().getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void 존재하지_않는_예약_시간을_삭제하면_예외가_발생한다() {
        // given
        reservationTimeRepository.failToDelete();

        // when & then
        assertThatThrownBy(() -> reservationTimeService.delete(1L))
                .isInstanceOf(NotFoundException.class);
    }

}
