package roomescape.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.domain.member.dto.MemberResponse;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.service.ReservationService;

import java.time.LocalDate;
import java.util.List;

import static roomescape.ReservationFixture.RESERVATION_TIME_TEN;
import static roomescape.ReservationFixture.THEME2;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    void findByMemberIdAndThemeIdAndDateBetween() {
        LocalDate from = LocalDate.of(2024, 5, 10);
        LocalDate to = LocalDate.of(2024, 5, 11);
        List<ReservationResponse> reservations = reservationService.findByMemberIdAndThemeIdAndDateBetween(1L, 2L, from, to);

        Assertions.assertThat(reservations).containsExactly(
                new ReservationResponse(1L, new MemberResponse(1L, "테니"), from, RESERVATION_TIME_TEN, THEME2),
                new ReservationResponse(2L, new MemberResponse(1L, "테니"), to, RESERVATION_TIME_TEN, THEME2)
        );
    }
}
