package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.dto.member.NameResponse;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.dto.theme.ThemeResponse;
import roomescape.dto.time.ReservationTimeResponse;
import roomescape.fixture.FakeReservationRepositoryFixture;
import roomescape.repository.ReservationRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReservationServiceTest {

    private final ReservationRepository reservationRepository = FakeReservationRepositoryFixture.create();
    private final ReservationService reservationService = new ReservationService(reservationRepository);

    @Nested
    @DisplayName("예약 조회")
    class FindReservation {

        @DisplayName("모든 Reservation을 조회할 수 있다")
        @Test
        void findAllReservationsTest() {
            // when
            List<ReservationResponse> responses = reservationService.findAllReservationResponses();

            // then
            assertAll(
                    () -> assertThat(responses).hasSize(1),
                    () -> assertThat(responses).extracting("member")
                            .containsExactly(new NameResponse(1L, "어드민")),
                    () -> assertThat(responses).extracting("date")
                            .containsExactly(LocalDate.now().plusDays(7)),
                    () -> assertThat(responses).extracting("theme")
                            .containsExactly(new ThemeResponse(1L, "우테코", "방탈출", "https://")),
                    () -> assertThat(responses).extracting("time")
                            .containsExactly(new ReservationTimeResponse(1L, LocalTime.of(10, 0)))
            );
        }

        @DisplayName("주어진 검색 조건에 해당하는 Reservation을 조회할 수 있다")
        @Test
        void searchReservationsTest() {
            // given
            long targetThemeId = 1L;
            long targetMemberId = 1L;
            LocalDate from = LocalDate.now();
            LocalDate to = LocalDate.now().plusDays(10);

            // when
            List<ReservationResponse> responses = reservationService.searchReservations(targetThemeId, targetMemberId, from, to);

            // then
            assertAll(
                    () -> assertThat(responses).hasSize(1),
                    () -> assertThat(responses).extracting("member")
                            .containsExactly(new NameResponse(1L, "어드민")),
                    () -> assertThat(responses).extracting("date")
                            .containsExactly(LocalDate.now().plusDays(7)),
                    () -> assertThat(responses).extracting("theme")
                            .containsExactly(new ThemeResponse(1L, "우테코", "방탈출", "https://")),
                    () -> assertThat(responses).extracting("time")
                            .containsExactly(new ReservationTimeResponse(1L, LocalTime.of(10, 0)))
            );
        }
    }
}
