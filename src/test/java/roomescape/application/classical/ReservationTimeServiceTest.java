package roomescape.application.classical;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.application.ReservationTimeService;
import roomescape.application.dto.ReservationTimeCreationRequest;
import roomescape.domain.time.ReservationTime;
import roomescape.support.extension.TableTruncateExtension;

@SpringBootTest
@ExtendWith(TableTruncateExtension.class)
public class ReservationTimeServiceTest {
    @Autowired
    private ReservationTimeService reservationTimeService;

    @Test
    void 예약_시간을_성공적으로_등록한다() {
        LocalTime startAt = LocalTime.of(13, 0);
        ReservationTimeCreationRequest request = new ReservationTimeCreationRequest(startAt);

        ReservationTime reservationTime = reservationTimeService.register(request);

        assertThat(reservationTime.getStartAt()).isEqualTo(startAt);
    }

    @Test
    void 중복된_예약_시간이_있으면_등록을_실패한다() {
        LocalTime startAt = LocalTime.of(13, 0);
        ReservationTimeCreationRequest request = new ReservationTimeCreationRequest(startAt);
        reservationTimeService.register(request);

        assertThatThrownBy(() -> reservationTimeService.register(request))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 존재하는 예약 시간입니다.");
    }

    @Test
    void 예약_시간을_삭제한다() {
        LocalTime startAt = LocalTime.of(13, 0);
        ReservationTimeCreationRequest request = new ReservationTimeCreationRequest(startAt);
        ReservationTime reservationTime = reservationTimeService.register(request);

        reservationTimeService.delete(reservationTime.getId());

        assertThat(reservationTimeService.getReservationTimes()).isEmpty();
    }

    @Test
    void 존재하지_않는_예약_시간을_삭제하면_예외가_발생한다() {
        assertThatThrownBy(() -> reservationTimeService.delete(1L))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 예약 시간입니다.");
    }
}
