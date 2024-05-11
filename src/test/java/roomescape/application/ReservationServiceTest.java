package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.jdbc.Sql;
import roomescape.application.dto.request.ReservationCreationRequest;
import roomescape.application.dto.response.ReservationResponse;
import roomescape.support.annotation.FixedClock;
import roomescape.support.annotation.WithoutWebSpringBootTest;

@WithoutWebSpringBootTest
@FixedClock(date = "2024-04-20")
@Sql("/reservation.sql")
public class ReservationServiceTest {
    @Autowired
    private ReservationService reservationService;

    @Test
    void 예약을_성공한다() {
        LocalDate date = LocalDate.parse("2024-04-21");
        ReservationCreationRequest request = new ReservationCreationRequest(date, 1L, 1L, 1L);

        ReservationResponse response = reservationService.reserve(request);

        assertAll(
                () -> assertThat(response.date()).isEqualTo(request.date()),
                () -> assertThat(response.time().id()).isEqualTo(request.timeId()),
                () -> assertThat(response.theme().id()).isEqualTo(request.themeId())
        );
    }

    @Test
    void 최소_1일_전에_예약하지_않으면_예약을_실패한다() {
        LocalDate invalidDate = LocalDate.parse("2024-04-20");
        ReservationCreationRequest request = new ReservationCreationRequest(invalidDate, 1L, 1L, 1L);

        assertThatThrownBy(() -> reservationService.reserve(request))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("예약은 최소 1일 전에 해야합니다.");
    }

    @Test
    void 중복된_예약이_있으면_예약을_실패한다() {
        LocalDate date = LocalDate.parse("2024-05-01");
        ReservationCreationRequest request = new ReservationCreationRequest(date, 1L, 1L, 1L);

        assertThatThrownBy(() -> reservationService.reserve(request))
                .isExactlyInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void 예약을_취소한다() {
        reservationService.cancel(2L);

        assertThat(reservationService.findReservations()).hasSize(9);
    }

    @Test
    void 존재하지_않는_예약을_취소하면_예외가_발생한다() {
        assertThatThrownBy(() -> reservationService.cancel(0L))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 예약입니다.");
    }
}
