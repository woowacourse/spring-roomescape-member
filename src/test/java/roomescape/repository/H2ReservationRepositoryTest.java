package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Transactional
@SpringBootTest
class H2ReservationRepositoryTest {
    @Autowired
    private H2ReservationRepository reservationRepository;

    @Autowired
    private H2ReservationTimeRepository reservationTimeRepository;

    @DisplayName("예약을 저장한다")
    @Test
    void save() {
        ReservationTime savedReservationTime = reservationTimeRepository.save(new ReservationTime(
                LocalTime.of(12, 12, 12)));
        Reservation savedReservation = reservationRepository.save(new Reservation(
                "비밥",
                LocalDate.of(2024, 4, 20),
                savedReservationTime));

        assertSoftly(softly -> {
            assertThat(savedReservation.getName())
                    .isEqualTo(new Name("비밥"));
            assertThat(savedReservation.getDate())
                    .isEqualTo(LocalDate.of(2024, 4, 20));
            assertThat(savedReservation.getTime().getStartAt())
                    .isEqualTo(LocalTime.of(12, 12, 12));
            assertThat(savedReservation.getTime().getId())
                    .isEqualTo(savedReservationTime.getId());
        });
    }

    @DisplayName("저장된 예약을 조회한다")
    @Test
    void findAll() {
        ReservationTime savedReservationTime = reservationTimeRepository.save(new ReservationTime(
                LocalTime.of(12, 12, 12)));
        reservationRepository.save(new Reservation(
                "비밥",
                LocalDate.now().plusDays(1),
                savedReservationTime));
        reservationRepository.save(new Reservation(
                "망쵸",
                LocalDate.now().plusDays(1),
                savedReservationTime));

        assertThat(reservationRepository.findAll())
                .hasSize(2);
    }

    @DisplayName("저장된 예약을 삭제한다")
    @Test
    void deleteById() {
        ReservationTime savedReservationTime = reservationTimeRepository.save(new ReservationTime(
                LocalTime.of(12, 12, 12)));
        Reservation savedReservation = reservationRepository.save(new Reservation(
                "비밥",
                LocalDate.now().plusDays(1),
                savedReservationTime));

        reservationRepository.deleteById(savedReservation.getId());

        assertThat(reservationRepository.findAll())
                .hasSize(0);
    }

    @DisplayName("삭제할 예약이 존재하지 않는다")
    @Test
    void deleteById_reservationDoesNotExist() {
        assertThatCode(() -> reservationRepository.deleteById(1))
                .doesNotThrowAnyException();
    }
}
