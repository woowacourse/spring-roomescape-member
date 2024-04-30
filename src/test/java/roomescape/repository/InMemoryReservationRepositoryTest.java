package roomescape.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.fake.InMemoryReservationRepository;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
class InMemoryReservationRepositoryTest {
    private final InMemoryReservationRepository reservationRepository = new InMemoryReservationRepository();

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
    }

    @DisplayName("예약을 저장한다")
    @Test
    void save() {
        Reservation reservation = new Reservation(
                "비밥",
                LocalDate.of(2024, 4, 20),
                LocalTime.of(12, 12, 12));

        Reservation savedReservation = reservationRepository.save(reservation);

        assertSoftly(softly -> {
            assertThat(savedReservation.getName())
                    .isEqualTo(new Name("비밥"));
            assertThat(savedReservation.getDate())
                    .isEqualTo(LocalDate.of(2024, 4, 20));
            assertThat(savedReservation.getTime().getStartAt())
                    .isEqualTo(LocalTime.of(12, 12, 12));
        });
    }

    @DisplayName("저장된 예약을 조회한다")
    @Test
    void findAll() {
        reservationRepository.save(new Reservation(
                "비밥",
                LocalDate.of(2024, 4, 20),
                LocalTime.of(12, 12, 12)));
        reservationRepository.save(new Reservation(
                "망쵸",
                LocalDate.of(2024, 4, 20),
                LocalTime.of(11, 11, 11)));

        assertThat(reservationRepository.findAll())
                .hasSize(2);
    }

    @DisplayName("저장된 예약을 삭제한다")
    @Test
    void deleteById() {
        Reservation savedReservation = reservationRepository.save(new Reservation(
                "비밥",
                LocalDate.of(2024, 4, 20),
                LocalTime.of(12, 12, 12)));

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
