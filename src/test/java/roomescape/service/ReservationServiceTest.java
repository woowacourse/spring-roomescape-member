package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.dto.ReservationRequestDto;
import roomescape.model.Reservation;
import roomescape.repository.MemoryReservationRepository;
import roomescape.repository.MemoryReservationTimeRepository;

class ReservationServiceTest {
    private final MemoryReservationRepository memoryReservationRepository = new MemoryReservationRepository();
    private final ReservationTimeService reservationTimeService = new ReservationTimeService(
            new MemoryReservationTimeRepository(), (Long id) -> {return true;});
    private final ReservationService reservationService = new ReservationService(memoryReservationRepository,
            reservationTimeService);

    @Nested
    class ReservationCreateTest {
        @Test
        @DisplayName("예약 생성")
        void test1() {
            // given
            Long timeId = reservationTimeService.addTime(LocalTime.parse("10:00")).getId();

            // when
            Reservation reservation = reservationService.addReservation(
                    new ReservationRequestDto("테스트", LocalDate.parse("2025-05-05"), timeId));

            // then
            assertAll(() -> assertThat(reservation).isNotNull(),
                    () -> assertThat(reservation.getId()).isNotNull(),
                    () -> assertThat(reservation.getUserName().getName()).isEqualTo("테스트")
            );
        }

        @Test
        @DisplayName("과거 예약 방지")
        void test2() {
            // given
            Long timeId = reservationTimeService.addTime(LocalTime.parse("10:00")).getId();

            // when & then
            assertThatThrownBy(() -> reservationService.addReservation(
                    new ReservationRequestDto("과거맨", LocalDate.parse("1999-05-05"), timeId))).hasMessage(
                    "과거 예약은 불가능합니다.");
        }
    }

    @Test
    @DisplayName("예약 전체 조회")
    void test2() {
        // given
        Long timeId_1 = reservationTimeService.addTime(LocalTime.parse("10:00")).getId();
        Long timeId_2 = reservationTimeService.addTime(LocalTime.parse("11:00")).getId();
        Reservation reservation = reservationService.addReservation(
                new ReservationRequestDto("띠용", LocalDate.parse("2025-05-05"), timeId_1));
        Reservation reservation2 = reservationService.addReservation(
                new ReservationRequestDto("구구", LocalDate.parse("2025-05-06"), timeId_2));

        // when & then
        assertThat(reservationService.getAllReservations()).contains(reservation, reservation2);
    }

    @Test
    @DisplayName("예약 삭제")
    void test3() {
        // given
        Long timeId = reservationTimeService.addTime(LocalTime.parse("10:00")).getId();
        Reservation reservation = reservationService.addReservation(
                new ReservationRequestDto("띠용", LocalDate.parse("2025-05-05"), timeId));

        // when
        reservationService.deleteReservation(reservation.getId());

        // then
        assertThat(reservationService.getAllReservations()).doesNotContain(reservation);
    }
}
