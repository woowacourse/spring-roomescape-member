package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.reservation.common.BaseTest;
import roomescape.reservation.controller.request.ReservationCreateRequest;
import roomescape.reservation.controller.response.ReservationResponse;
import roomescape.time.controller.response.ReservationTimeResponse;
import roomescape.time.domain.ReservationTime;
import roomescape.time.service.ReservationTimeRepository;

public class ReservationServiceTest extends BaseTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    void 예약을_생성한다() {
        createTime(ReservationTime.create(LocalTime.of(10, 0)));
        ReservationCreateRequest request = new ReservationCreateRequest(
                "폰트",
                LocalDate.of(2025, 4, 30),
                1L
        );

        ReservationResponse response = reservationService.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("폰트");
        assertThat(response.date()).isEqualTo(LocalDate.of(2025, 4, 30));
        assertThat(response.time()).isEqualTo(new ReservationTimeResponse(1L, "10:00"));
    }

    @Test
    void 예약이_존재하면_예약을_생성할_수_없다() {
        createTime(ReservationTime.create(LocalTime.of(10, 0)));
        reservationService.create(new ReservationCreateRequest(
                "폰트",
                LocalDate.of(2025, 5, 30),
                1L
        ));

        ReservationCreateRequest request = new ReservationCreateRequest(
                "폰트",
                LocalDate.of(2025, 5, 30),
                1L
        );
        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 예약을_모두_조회한다() {
        createTime(ReservationTime.create(LocalTime.of(10, 0)));
        reservationService.create(new ReservationCreateRequest(
                "폰트",
                LocalDate.of(2025, 5, 30),
                1L
        ));
        List<ReservationResponse> responses = reservationService.getAll();

        assertThat(responses.get(0)).isEqualTo(new ReservationResponse(
                1L,
                "폰트",
                LocalDate.of(2025, 5, 30),
                new ReservationTimeResponse(1L, "10:00")));
    }

    @Test
    void 예약을_삭제한다() {
        createTime(ReservationTime.create(LocalTime.of(10, 0)));
        reservationService.create(new ReservationCreateRequest(
                "폰트",
                LocalDate.of(2025, 5, 30),
                1L
        ));
        reservationService.deleteById(1L);

        List<ReservationResponse> responses = reservationService.getAll();

        assertThat(responses).hasSize(0);
    }

    @Test
    void 존재하지_않는_예약을_삭제할_수_없다() {
        assertThatThrownBy(() -> reservationService.deleteById(3L))
                .isInstanceOf(NoSuchElementException.class);
    }

    private ReservationTime createTime(ReservationTime reservationTime) {
        return reservationTimeRepository.save(reservationTime);
    }
}
