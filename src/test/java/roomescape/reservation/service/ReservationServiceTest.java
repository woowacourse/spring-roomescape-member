package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import roomescape.reservation.controller.request.ReservationCreateRequest;
import roomescape.reservation.controller.response.ReservationResponse;
import roomescape.reservation.service.config.ReservationTestConfig;
import roomescape.time.controller.response.ReservationTimeResponse;

@SpringJUnitConfig(classes = ReservationTestConfig.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    void 예약을_생성한다() {
        ReservationCreateRequest request = new ReservationCreateRequest(
                "폰트",
                LocalDate.of(2025, 4, 30),
                1L
        );

        ReservationResponse response = reservationService.create(request);

        assertThat(response.id()).isEqualTo(3L);
        assertThat(response.name()).isEqualTo("폰트");
        assertThat(response.date()).isEqualTo(LocalDate.of(2025, 4, 30));
        assertThat(response.time()).isEqualTo(new ReservationTimeResponse(1L, "10:00"));
    }

    @Test
    void 예약을_모두_조회한다() {
        List<ReservationResponse> responses = reservationService.getAll();

        assertThat(responses.get(0)).isEqualTo(new ReservationResponse(
                1L,
                "폰트",
                LocalDate.of(2025, 4, 1),
                new ReservationTimeResponse(1L, "10:00")));
        assertThat(responses.get(1)).isEqualTo(new ReservationResponse(
                2L,
                "폰트",
                LocalDate.of(2025, 4, 1),
                new ReservationTimeResponse(2L, "11:00")));
    }

    @Test
    void 예약을_삭제한다() {
        reservationService.deleteById(1L);

        List<ReservationResponse> responses = reservationService.getAll();

        assertThat(responses).hasSize(1);
    }

    @Test
    void 존재하지_않는_예약을_삭제할_수_없다() {
        assertThatThrownBy(() -> reservationService.deleteById(3L))
                .isInstanceOf(NoSuchElementException.class);
    }
}
