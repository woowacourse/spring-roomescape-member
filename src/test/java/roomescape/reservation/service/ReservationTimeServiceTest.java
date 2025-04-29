package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import roomescape.reservation.service.config.ReservationTestConfig;
import roomescape.time.controller.request.ReservationTimeCreateRequest;
import roomescape.time.controller.response.ReservationTimeResponse;
import roomescape.time.service.ReservationTimeService;

@SpringJUnitConfig(classes = ReservationTestConfig.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Test
    void 예약시간을_생성한다() {
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(LocalTime.of(10, 0));

        ReservationTimeResponse response = reservationTimeService.create(request);

        assertThat(response.id()).isEqualTo(3L);
        assertThat(response.startAt()).isEqualTo("10:00");
    }

    @Test
    void 예약시간을_모두_조회한다() {
        List<ReservationTimeResponse> responses = reservationTimeService.getAll();

        assertThat(responses.get(0).startAt()).isEqualTo("10:00");
        assertThat(responses.get(1).startAt()).isEqualTo("11:00");
    }

    @Test
    void 예약시간을_삭제한다() {
        reservationTimeService.deleteById(1L);

        List<ReservationTimeResponse> responses = reservationTimeService.getAll();

        assertThat(responses).hasSize(1);
    }

    @Test
    void 존재하지_않는_예약시간을_삭제할_수_없다() {
        assertThatThrownBy(() -> reservationTimeService.deleteById(3L))
                .isInstanceOf(NoSuchElementException.class);
    }
}
