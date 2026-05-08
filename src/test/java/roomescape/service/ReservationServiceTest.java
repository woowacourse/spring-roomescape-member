package roomescape.service;

import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dto.ReservationRequest;

@SpringBootTest
@Transactional
public class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    void 존재하지_않는_예약을_삭제할_경우_예외가_발생한다() {
        // when
        Assertions.assertThatThrownBy(() -> reservationService.removeById(-1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하는_예약에_대해서_삭제할_수_있다() {
        // when
        Assertions.assertThatCode(() -> reservationService.removeById(1L))
                .doesNotThrowAnyException();
    }

    @Test
    void 존재하는_예약을_추가할_경우_예외가_발생한다() {
        // given
        ReservationRequest reservationRequest = new ReservationRequest("포비", LocalDate.of(2026, 5, 1), 1L, 1L);

        // when
        Assertions.assertThatThrownBy(() -> reservationService.register(reservationRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_예약을_정상적으로_추가할_수_있다() {
        // given
        ReservationRequest reservationRequest = new ReservationRequest("무빙", LocalDate.of(2026, 5, 3), 2L, 2L);

        // when
        Assertions.assertThatCode(() -> reservationService.register(reservationRequest))
                .doesNotThrowAnyException();
    }
}
