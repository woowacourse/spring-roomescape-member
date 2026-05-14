package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.controller.dto.ReservationTimeCreateRequest;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    void 시간을_생성한다() {
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest("10:00");
        ReservationTime saved = ReservationTime.of(1L, "10:00");
        given(reservationTimeRepository.save(any())).willReturn(saved);

        ReservationTime result = reservationTimeService.create(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStartAt().toString()).isEqualTo("10:00");
    }

    @Test
    void 전체_시간을_조회한다() {
        List<ReservationTime> times = List.of(
                ReservationTime.of(1L, "10:00"),
                ReservationTime.of(2L, "11:00")
        );
        given(reservationTimeRepository.findAll()).willReturn(times);

        List<ReservationTime> result = reservationTimeService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void id로_시간을_조회한다() {
        ReservationTime time = ReservationTime.of(1L, "10:00");
        given(reservationTimeRepository.findById(1L)).willReturn(Optional.of(time));

        ReservationTime result = reservationTimeService.find(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void 존재하지_않는_시간_조회시_예외가_발생한다() {
        given(reservationTimeRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reservationTimeService.find(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 시간을_삭제한다() {
        ReservationTime time = ReservationTime.of(1L, "10:00");
        given(reservationTimeRepository.findById(1L)).willReturn(Optional.of(time));

        reservationTimeService.delete(1L);

        verify(reservationTimeRepository).delete(1L);
    }

    @Test
    void 존재하지_않는_시간_삭제시_예외가_발생한다() {
        given(reservationTimeRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reservationTimeService.delete(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("요청한 시간을 찾을 수 없습니다");
    }
}
